// Teoria Współbieżnośi, implementacja problemu 5 filozofów w node.js
// Opis problemu: http://en.wikipedia.org/wiki/Dining_philosophers_problem
//   https://pl.wikipedia.org/wiki/Problem_ucztuj%C4%85cych_filozof%C3%B3w
// 1. Dokończ implementację funkcji podnoszenia widelca (Fork.acquire).
// 2. Zaimplementuj "naiwny" algorytm (każdy filozof podnosi najpierw lewy, potem
//    prawy widelec, itd.).
// 3. Zaimplementuj rozwiązanie asymetryczne: filozofowie z nieparzystym numerem
//    najpierw podnoszą widelec lewy, z parzystym -- prawy. 
// 4. Zaimplementuj rozwiązanie z kelnerem (według polskiej wersji strony)
// 5. Zaimplementuj rozwiążanie z jednoczesnym podnoszeniem widelców:
//    filozof albo podnosi jednocześnie oba widelce, albo żadnego.
// 6. Uruchom eksperymenty dla różnej liczby filozofów i dla każdego wariantu
//    implementacji zmierz średni czas oczekiwania każdego filozofa na dostęp 
//    do widelców. Wyniki przedstaw na wykresach.

var Fork = function() {
    this.state = 0;
    return this;
}

Fork.prototype.acquire = function(cb) { 
    // zaimplementuj funkcję acquire, tak by korzystala z algorytmu BEB
    // (http://pl.wikipedia.org/wiki/Binary_Exponential_Backoff), tzn:
    // 1. przed pierwszą próbą podniesienia widelca Filozof odczekuje 1ms
    // 2. gdy próba jest nieudana, zwiększa czas oczekiwania dwukrotnie
    //    i ponawia próbę, itd.
	var fork = this,
	trying = function(delay) {
		setTimeout(function() {
			if(fork.state == 0) {
				fork.state = 1;
				cb();
			}
			else {
				trying(delay*2);
			}
		}, delay);
	};
	
	trying(1);
}

Fork.prototype.release = function() { 
    this.state = 0; 
}

var Philosopher = function(id, forks) {
    this.id = id;
    this.forks = forks;
    this.f1 = id % forks.length;
    this.f2 = (id+1) % forks.length;
	this.thinkingTime = 0;
	this.tmpThinkingTime = 0;
	
    return this;
}

Philosopher.prototype.startNaive = function(count) {
    this.thinkingTime = 0;
	
	var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id,
		phil = this,
		
    
    // zaimplementuj rozwiązanie naiwne
    // każdy filozof powinien 'count' razy wykonywać cykl
    // podnoszenia widelców -- jedzenia -- zwalniania widelców
	
	tryingNaive = function(count) {
		phil.tmpThinkingTime = new Date().getTime();
		if(count > 0) {
			forks[f1].acquire(function () {
				console.log("Philosopher " + id + " took first fork");
				forks[f2].acquire(function () {
					phil.thinkingTime += new Date().getTime()-phil.tmpThinkingTime;
					console.log("Philosopher " + id + " took second fork and starts eating");
					setTimeout(function () {
						console.log("Philosopher " + id + " ends eating");
						forks[f1].release();
						forks[f2].release();
						tryingNaive(count-1);
			}, 10)
		    })})
		}
		else {
			hungryPhilosophers--;
		}
		if(hungryPhilosophers == 0) {
			getTimes();
		}
	};
	
	setTimeout(function () {tryingNaive(count)}, 10);
}

Philosopher.prototype.startAsym = function(count) {
	this.thinkingTime = 0;
	
    var forks = this.forks,
		phil = this,
		f1 = this.f1,
		f2 = this.f2,
        id = this.id;
	
	if(id % 2 == 0) {
        f1 = this.f2;
        f2 = this.f1;
	}
	
	var tryingAsym = function(count) {
		phil.tmpThinkingTime = new Date().getTime();
		if(count > 0) {
			forks[f1].acquire(function () {
				console.log("Philosopher " + id + " took first fork");
				forks[f2].acquire(function () {
					phil.thinkingTime += new Date().getTime()-phil.tmpThinkingTime;
					console.log("Philosopher " + id + " took second fork and starts eating");
					setTimeout(function () {
						console.log("Philosopher " + id + " ends eating");
						forks[f1].release();
						forks[f2].release();
						tryingAsym(count-1);
			}, 10)
		    })})
		}
		else {
			hungryPhilosophers--;
		}
		if(hungryPhilosophers == 0) {
			getTimes();
		}
	};
	
	setTimeout(function () {tryingAsym(count)}, 10);
    
    // zaimplementuj rozwiązanie asymetryczne
    // każdy filozof powinien 'count' razy wykonywać cykl
    // podnoszenia widelców -- jedzenia -- zwalniania widelców
}

Philosopher.prototype.startConductor = function(count, conductor) {
	this.thinkingTime = 0;
	
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id,
		phil = this,
    
    // zaimplementuj rozwiązanie z kelnerem
    // każdy filozof powinien 'count' razy wykonywać cykl
    // podnoszenia widelców -- jedzenia -- zwalniania widelców
	tryingConductor = function (count) {
		phil.tmpThinkingTime = new Date().getTime();
	    if (count > 0) {
			conductor.enterRoom(id, function () {
				console.log("Philosopher " + id + " entered the room");
				acquireBothForks(forks[f1], forks[f2], function () {
						phil.thinkingTime += new Date().getTime()-phil.tmpThinkingTime;
						console.log("Philosopher " + id + " took forks and starts eating");
						setTimeout(function () {
							forks[f1].release();
							forks[f2].release();
							console.log("Philosopher " + id + " ends eating and leaves room");
							conductor.leaveRoom(id, function () {
								tryingConductor(count-1);
					});
			    }, 10)
			})})
		}
		else {
			hungryPhilosophers--;
		}
		if(hungryPhilosophers == 0) {
			getTimes();
		}
	};
	setTimeout(function () {tryingConductor(count)}, 10);
}

Cond = function(N) {
	this.N = N;
	this.philosophersInRoom = 0;
	this.cbs = [];
    this.thinkingPhilosophers = [];
    
    return this;
}

Cond.prototype.enterRoom = function(philosopherIdx, cb) {
    if (this.philosophersInRoom < this.N-1) {
		this.philosophersInRoom++;
		setTimeout(cb);
    }
    else {
		var pushBack = function(waiting, ob) {
			waiting.push(ob);
			for (var i = waiting.length-1; i > 0; i--) {
				waiting[i] = waiting[i-1];
			}
			waiting[0] = ob;
		};

		pushBack(this.cbs, cb);
		pushBack(this.thinkingPhilosophers, philosopherIdx);
    }
}

Cond.prototype.leaveRoom = function(philosopherIdx, cb) {
    this.philosophersInRoom--;

    while (this.cbs.length > 0 && this.philosophersInRoom < this.N-1) {
		var philosopherIdxFromList = this.thinkingPhilosophers.pop(),
	    callBackFromList = this.cbs.pop();
	
		this.philosophersInRoom++;
		
		setTimeout(callBackFromList);
    }

    setTimeout(cb);
}

function acquireBothForks(left, right, cb) { 
    // zaimplementuj funkcję acquire, tak by korzystala z algorytmu BEB
    // (http://pl.wikipedia.org/wiki/Binary_Exponential_Backoff), tzn:
    // 1. przed pierwszą próbą podniesienia widelca Filozof odczekuje 1ms
    // 2. gdy próba jest nieudana, zwiększa czas oczekiwania dwukrotnie
    //    i ponawia próbę, itd.
	var trying = function(delay) {
		setTimeout(function() {
			if(left.state == 0 && right.state == 0) {
				left.state = 1;
				right.state = 1;
				cb();
			}
			else {
				trying(delay*2);
			}
		}, delay);
	};
	
	trying(1);
}


// TODO: wersja z jednoczesnym podnoszeniem widelców
// Algorytm BEB powinien obejmować podnoszenie obu widelców, 
// a nie każdego z osobna

Philosopher.prototype.startStarving = function(count) {
	this.thinkingTime = 0;

    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id,
		phil = this,
    
	// zaimplementuj rozwiązanie naiwne
	// każdy filozof powinien 'count' razy wykonywać cykl
	// podnoszenia widelców -- jedzenia -- zwalniania widelców

	tryingStarving = function (count) {
		phil.tmpThinkingTime = new Date().getTime();
	    if (count > 0) {
			acquireBothForks(forks[f1], forks[f2], function () {
				phil.thinkingTime += new Date().getTime()-phil.tmpThinkingTime;
				console.log("Philosopher " + id + " took forks and starts eating");
				setTimeout(function () {
					forks[f1].release();
					forks[f2].release();
					console.log("Philosopher " + id + " ends eating");
					tryingStarving(count-1);
				}, 10)
			})
		}
		else {
			hungryPhilosophers--;
		}
		if(hungryPhilosophers == 0) {
			getTimes();
		}
	};
    
    setTimeout(function () {tryingStarving(count)}, 10);
}

var N = 5;
var forks = [];
var philosophers = [];
var count = 10;
var hungryPhilosophers = N;
var getTimes;

for (var i = 0; i < N; i++) {
    forks.push(new Fork());
}

for (var i = 0; i < N; i++) {
    philosophers.push(new Philosopher(i, forks));
}

var cond = new Cond(philosophers.length),
filename;

var fs = require("fs");

for (var i = 0; i < N; i++) {
    philosophers[i].startAsym(count);
}

function deleteFile() {
	if(fs.existsSync(filename)) {
		fs.unlinkSync(filename, function(err){
			if(err) return console.log(err);
			console.log('File deleted successfully');
		});  
	}
}

function printAndSaveTimes() {
	deleteFile();
	for (var i = 0; i < N; i++) {
		var tim = philosophers[i].thinkingTime/count;
		console.log("Philosopher " + i + " average thinking time: " + tim + "ms");
		fs.appendFileSync(filename, i + " " + tim + "\n", function(err, result) {
			if(err) console.log('error', err);
		});
	}
	console.log("");
}

getTimes = function() {
	console.log("\nAsymetric results:");
	filename = N + "asym" + count + ".txt";
	printAndSaveTimes();
	
	hungryPhilosophers = N;
	
	for (var i = 0; i < N; i++) {
		philosophers[i].startConductor(count, cond);
	}
	
	getTimes = function() {
		console.log("\nConductor results:");
		filename = N + "cond" + count + ".txt";
		printAndSaveTimes();
	
		hungryPhilosophers = N;
		
		for (var i = 0; i < N; i++) {
			philosophers[i].startStarving(count, cond);
		}
		
		getTimes = function() {
			console.log("\nStarving results:");
			filename = N + "starv" + count + ".txt";
			printAndSaveTimes();
		}
	}
}

