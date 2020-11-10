gnuplot -e ^"set terminal png;^
set output '5philosophers10js.png';^
set title 'Average waiting time (5 philosophers, 10 counts) js';^
set xlabel 'Philosophers';^
set ylabel 'Thinking time [ms]';^
set style data histograms;^
set boxwidth 0.9;^
set style fill solid;^
set key vertical right above;^
plot '5asym10.txt' using 2:xtic(1) title 'Asymetric',^
     '5cond10.txt' using 2:xtic(1) title 'Conductor',^
	 '5starv10.txt' using 2:xtic(1) title 'Starving'"