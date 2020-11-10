import java.io.{FileNotFoundException, IOException}
import scala.collection.immutable.WrappedString
import scala.collection.mutable
import scala.io.Source

object Traces {
  val regex = "[^a-z]+"
  var transactions = Array.empty[WrappedString]
  var splitAlphabet: WrappedString = ""
  var splitWord: WrappedString = ""

  def loadDataFromFile(path: String): Unit = {
    try {
      val source = Source.fromFile(path)
      for (line <- source.getLines()) {
        println(line)
        if(line.exists(_.isUpper) && line.contains("=")) {
          splitAlphabet = line.replaceAll(regex, "").toSeq
        }

        if(!line.exists(_.isUpper) && line.contains("=") && !line.contains(":=")) {
          val tempSplit = line.split(" ").mkString("").split("=")
          splitWord = tempSplit(1).replaceAll(regex, "")
        }

        if(line.contains(')') && line.contains(":=")) {
          transactions = transactions :+ line.replaceAll(regex, "").toSeq
        }
      }
      println()
      source.close()

    } catch {
      case e: FileNotFoundException => println("Couldn't find that file.")
      case e: IOException => println("Had an IOException trying to read that file")
        System.exit(-1)
    }
  }

  def computeFoataNormalFormFromTrace(setI: IndexedSeq[(Char, Char)]): String = {
    //tworzymy mapę stosów, gdzie element mapy to stos dla odpowiedniego elementu alfabetu
    var stacks = Map[Char, mutable.Stack[Char]]()

    for(a <- splitAlphabet) stacks += (a -> mutable.Stack[Char]())

    val reversedWord = splitWord.reverse

    //wykonujemy algorytm podany w książce V. Diekert, Y. Metivier
    //– Partial commutation and traces, [w:] Handbook of Formal Languages
    for(a <- reversedWord) {
      stacks(a).push(a)
      for(b <- splitAlphabet) {
        if(!setI.contains((a , b)) && (stacks(b) ne stacks(a))) {
          stacks(b).push('*')
        }
      }
    }

    var foataNormalForm = "FNF = "

    while(stacks.nonEmpty) {
      var currentClass = ""
      for(a <- stacks) {
        if(a._2.isEmpty) {
          stacks = stacks.-(a._1)
        }
        else {
          if(a._2.top != '*') {
            currentClass += a._2.pop()
          }
        }
      }
      for(c <- currentClass) {
        for (a <- stacks) {
          if (a._2.nonEmpty
            && (a._2 ne stacks(c))
            && !setI.contains((a._1, c))
            && a._2.top == '*') {
            a._2.pop()
          }
        }
      }
      if(currentClass != "")
        foataNormalForm += "[" + currentClass + "]"
    }
    foataNormalForm
  }

  def computeFoataNormalFormFromGraph(setI: IndexedSeq[(Char, Char)],
                                      vA: Array[String],
                                      dG: List[(String, String)]): String = {
    var graphFoataForm = "FNF = "
    var vertexArray = vA
    var dependencyGraph = dG

    while(vertexArray.nonEmpty) {
      var currentClass = ""
      var currentClassNumber = Array[String]()
      for(a <- vertexArray) {
        var withoutPrevious = true
        for(b <- dependencyGraph if withoutPrevious) {
          if(b._2 == a) {
            withoutPrevious = false
          }
        }
        if(withoutPrevious) {
          currentClass += a(0)
          currentClassNumber = currentClassNumber :+ a.mkString("")
          vertexArray = vertexArray.filter(_ != a)
        }
      }
      for(c <- currentClassNumber) {
        dependencyGraph = dependencyGraph.filter(_._1 != c)
      }
      if(currentClass != "") {
        graphFoataForm += "[" + currentClass.toSeq.sortWith(_.compareTo(_) < 0).unwrap + "]"
      }
    }
    graphFoataForm
  }

  def translateToDotFormat(vertexArray: Array[String],
                           dependencyGraph: List[(String, String)]): String = {
    var dotFormat = "digraph g{\n"

    for(a <- dependencyGraph) {
      dotFormat += a._1(1) + " -> " + a._2(1) + "\n"
    }

    for(a <- vertexArray) {
      dotFormat += a(1) + "[label=" + a(0) + "]\n"
    }

    dotFormat += "}"

    dotFormat
  }

  def main(args: Array[String]) {
    if(args.length == 0) {
      println("Please provide file with data")
      System.exit(-1)
    }

    //wczytujemy dane z pliku
    println("For data:")
    loadDataFromFile(args(0))
    println("Results:")

    if(transactions.length == 0 || splitAlphabet.mkString("") == "" || splitWord.mkString("") == "") {
      println("Not proper data")
      System.exit(-1)
    }

    //obliczamy relację zależności
    val setD = for(a <- transactions; b <- transactions
                   if b.slice(2, b.length).contains(a(1))
                   || a.slice(2, a.length).contains(b(1))
                   || a == b) yield (a(0), b(0))

    //wyznaczamy wszystkie kombinacje transakcji na zmiennych
    val allPossibilities = for(a <- splitAlphabet; b <- splitAlphabet) yield (a, b)

    //obliczamy relację niezależności jako wszystkie możliwości - zbiór transakcji zależnych
    val setI = for(a <- allPossibilities if !setD.contains(a)) yield a

    //przygotowojumey relacje zależności i niezależności do wyświetlenia i je wypisujemy
    val readableD = "D = {" + setD.mkString(", ") + "}"
    val readableI = "I = {" + setI.mkString(", ") + "}"
    println(readableD)
    println(readableI)

    //obliczamy postać normalną Foaty
    val foataNormalForm = computeFoataNormalFormFromTrace(setI)

    println("\nFoata normal form computed from dependency set:\n" + foataNormalForm)

    var vertexArray = Array[String]()

    var dependencyGraph: List[(String, String)] = List[(String, String)]()

    //tworzymy graf zależności jako listę krawędzi w formacie (a1,b2), gdzie a1,b2 to wierzchołki
    // (liczna oznacza, na którym miejscu w słowie znajduje się dany symbol)
    for(i <- splitWord.length to 1 by -1) {
      val currentChar = splitWord(i-1)
      val currentCharNumber = currentChar.toString + i
      for(a <- vertexArray) {
        if(setD.contains((a(0), currentChar))) {
          dependencyGraph = (currentCharNumber, a) +: dependencyGraph
        }
      }
      vertexArray = currentCharNumber +: vertexArray
    }

    //minimalizujemy graf
    for(a <- vertexArray; b <- vertexArray; c <- vertexArray) {
          if(dependencyGraph.contains((a,b)) && dependencyGraph.contains((b,c))) {
            dependencyGraph = dependencyGraph.filter(_ != (a,c))
          }
    }

    //zapisujemy graf w formacie dot
    val dotFormat = translateToDotFormat(vertexArray, dependencyGraph)

    //wypisujemy graf w formacie dot
    println("\nGraph in dot format:\n" + dotFormat)

    //wyliczamy postać normalną Foaty na podstawie grafu
    val graphFoataForm = computeFoataNormalFormFromGraph(setI, vertexArray, dependencyGraph)

    //wypisujemy obliczoną postać normalną Foaty
    println("\nFoata normal form computed from graph:\n" + graphFoataForm)
  }
}