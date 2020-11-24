import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main {
    public static String getRandomColor() {
        Random rand = new Random();
        int rand_num = rand.nextInt(0xffffff + 1);

        return String.format("#%06x", rand_num);
    }
    public static void main(String[] args) {
        int n = 4;
        if(args.length > 0) {
            n = Integer.parseInt(args[0]);
        }
        String size = "10,8";
        StringBuilder graph = new StringBuilder("digraph {\nsize=\"" + size + "\"\n");
        for (int i = 1; i < n; i++) {
            for (int k = i + 1; k <= n; k++) {
                for (int j = i; j <= n + 1; j++) {
                    graph.append("A_").append(i).append("_").append(k)
                            .append(" -> B_").append(i).append("_")
                            .append(j).append("_").append(k).append(";\n");

                    graph.append("B_").append(i).append("_").append(j)
                            .append("_").append(k).append(" -> C_")
                            .append(i).append("_").append(j).append("_")
                            .append(k).append(";\n");
                }
            }
        }

        for (int i_1 = 1; i_1 < n; i_1++)
        {
            int i_2 = i_1 + 1;
            int k_1 = i_2;
            for (int j = i_2 + 1; j <= n + 1; j++) {
                for (int k_2 = i_2 + 1; k_2 <= n; k_2++) {
                    graph.append("C_").append(i_1).append("_").append(j)
                            .append("_").append(k_1).append(" -> B_")
                            .append(i_2).append("_").append(j).append("_")
                            .append(k_2).append(";\n");
                }
            }
        }

        for (int i_1 = 1; i_1 < n - 1; i_1++)
        {
            int i_2 = i_1 + 1;
            for (int k = i_2 + 1; k <= n; k++)
                for (int j = i_2 + 1; j <= n + 1; j++)
                    graph.append("C_").append(i_1).append("_").append(j)
                            .append("_").append(k).append(" -> C_")
                            .append(i_2).append("_").append(j)
                            .append("_").append(k).append(";\n");
        }

        for (int i_1 = 1; i_1 < n - 1; i_1++)
        {
            int i_2 = i_1 + 1;
            int j = i_2;
            int k_1 = i_2;
            for (int k_2 = i_2 + 1; k_2 <= n; k_2++) {
                graph.append("C_").append(i_1).append("_").append(j)
                        .append("_").append(k_1).append(" -> A_")
                        .append(i_2).append("_").append(k_2).append(";\n");
            }
            for (k_1 = i_2 + 1; k_1 <= n; k_1++)
            {
                int k_2 = k_1;
                graph.append("C_").append(i_1).append("_").append(j)
                        .append("_").append(k_1).append(" -> A_")
                        .append(i_2).append("_").append(k_2).append(";\n");
            }
        }

        for (int i = 1; i < n; i++)
        {
            String color_A = getRandomColor();
            String color_B = getRandomColor();
            String color_C = getRandomColor();

            for (int k = i + 1; k <= n; k++)
            {
                graph.append("A_").append(i).append("_").append(k)
                        .append(" [label=<A<sub>").append(i)
                        .append(",").append(k).append("</sub>>, ")
                        .append("fillcolor=\"").append(color_A)
                        .append("\", style=filled];\n");
                for (int j = i; j <= n + 1; j++)
                    graph.append("C_").append(i).append("_").append(j)
                            .append("_").append(k).append(" [label=<C<sub>")
                            .append(i).append(",").append(j).append(",")
                            .append(k).append("</sub>>, ").append("fillcolor=\"")
                            .append(color_C).append("\", style=filled];\n")
                            .append("B_").append(i).append("_").append(j)
                            .append("_").append(k).append(" [label=<B<sub>")
                            .append(i).append(",").append(j).append(",")
                            .append(k).append("</sub>>, ").append("fillcolor=\"")
                            .append(color_B).append("\", style=filled];\n");
            }
        }
        graph.append("}");
        System.out.println(graph);
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("graph.dot", false));
            out.write(graph.toString());
            out.close();
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
