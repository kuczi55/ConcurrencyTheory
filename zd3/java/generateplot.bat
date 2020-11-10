gnuplot -e ^"set terminal png;^
set output '15philosophers10java.png';^
set title 'Average waiting time (15 philosophers, 10 counts) java';^
set xlabel 'Philosophers';^
set ylabel 'Thinking time [ms]';^
set xzeroaxis;^
set style data histograms;^
set yrange [0:*];^
set boxwidth 0.9;^
set style fill solid;^
set key vertical right above;^
plot '15arbiter10.txt' using 2:xtic(1) title 'Conductor',^
	 '15starv10.txt' using 2:xtic(1) title 'Starving'"