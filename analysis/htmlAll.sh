#!/bin/bash

datasetDir="../datasets"

function header {
	echo '<?php'
	# echo '$path = str_replace("/Users/benni/TUD/Projects/DNA/DNA.webpage/web/", "", getcwd());'
	echo '$path = ltrim(str_replace($_SERVER["DOCUMENT_ROOT"], "", getcwd()), "/");'
	echo '$dirs = split("/", $path);'
	echo '$pre = "";'
	echo 'for($i = 0; $i < count($dirs); $i++) { $pre .= "../"; }'
	echo 'require($pre."layout/header.php");'
	echo '?>'
}

function footer {
	echo '<?php require($pre."layout/footer.php"); ?>';
}

function img {
	dir=$datasetDir/$et/$name
	img=$1
	title=$2
	if [[ -f $dir/$plots/$img ]]; then
		echo "<a href='$plots/$img'><img src='$plots/$img' alt='$title for $name' title='$title for $name' width='400'/></a>"
	fi
}

function process_dir {
	header
	echo '<ul>'
	for name in $(ls $1 | grep -v index); do
		if [[ -e "$datasetDir/$et/$name/README.$name" ]]; then
			title=$(head -1 "$datasetDir/$et/$name/README.$name" | cut -d ',' -f1)
			description=$(head -6 "$datasetDir/$et/$name/README.$name" | tail -1)
			plots=$(ls $datasetDir/$et/$name/ | grep '_plots')
			if [[ $plots != '' ]]; then
				echo "<li><a href='$name/' style='font-size:12pt; font-weight:bold;'>$title ($name)</a></li>"
			else
				echo "<li><a href='$name/' style='font-size:12pt; font-weight:normal;'>$title ($name)</a></li>"
			fi
		else
			echo "<li><a href='$name/' style='font-size:12pt;'>$name</a></li>"
		fi
	done
	echo '</ul>'
	footer
}

function process_dataset {
	header
	datasetDir=$1
	et=$2
	name=$3

	a=$(ls $datasetDir/$et/$name/ | grep -v README | wc -l | xargs)
	if [[ "$a" -lt "1" ]]; then continue; fi
	title=$(head -1 "$datasetDir/$et/$name/README.$name" | cut -d ',' -f1)
	description=$(head -6 "$datasetDir/$et/$name/README.$name" | tail -1)
	url=$(head -10 "$datasetDir/$et/$name/README.$name" | tail -1)
	html="$datasetDir/$et/$name/$name.html"

	if [[ -n "$(cat $html | grep 'B.png')" ]]; then connection="B"
	elif [[ -n "$(cat $html | grep 'D.png')" ]]; then connection="D"
	elif [[ -n "$(cat $html | grep 'U.png')" ]]; then connection="U"
	else connection="unknown"
	fi

	echo "<h2>$name - $title</h2>"
	echo "<div><img src='../../../img/$connection.png' title='$connection' alt='$connection'/> <img src='../../../img/$et.png' title='$et' alt='$et'/></div>"
	echo "<br class='clear'/>"
	echo "<div><i>$description</i></div>"
	echo "<br class='clear'/>"
	echo "<div>Konect sources: <a href='$url'>$url</a></div>"
	echo "<br class='clear'/>"

	plots=$(ls $datasetDir/$et/$name/ | grep '_plots')
	if [[ $plots != '' ]]; then
		echo "<div><h2>Analysis / Results / Plots</h2>"
		echo "Parameters: $plots"
		echo "<br class='clear'/>"
		echo "<br class='clear'/>"

		echo "<br class='clear'/><h2>Statistics</h2>"
		img z.statistics.nodes.png 'node count'
		img z.statistics.updates.nodes.png 'node updates'
		img z.statistics.edges.png 'edge count'
		img z.statistics.updates.edges.png 'edge updates'
		echo "<br class='clear'/>"
		echo "<br class='clear'/><h2>Assortativity</h2>"
		img AssortativityR-out-unweighted.AssortativityCoefficient.png 'assortativity'
		echo "<br class='clear'/>"
		echo "<br class='clear'/><h2>Degree Distribution</h2>"
		img DegreeDistributionR.degreeMin.png 'min degree'
		img DegreeDistributionR.degreeMax.png 'max degree'
		echo "<br class='clear'/>"
		echo "<br class='clear'/><h2>Edge Weights</h2>"
		img EdgeWeightsR-1.0.MinWeight.png 'min edge weight'
		img EdgeWeightsR-1.0.AverageWeight.png 'avg edge weight'
		img EdgeWeightsR-1.0.MaxWeight.png 'max edge weight'
		echo "<br class='clear'/>"
		echo "<br class='clear'/><h2>Memory & Runtimes</h2>"
		img z.statistics.memory.png 'memory usage'
		img z.runtimes.0.all.CDF.png 'runtimes (CDF)'
		img z.runtimes.3.allMetrics.CDF.png 'metric runtimes (CDF)'

		echo "</div>"
	else
		echo "<div>this dataset has not been analyzed yet</div>"
	fi

	footer
}

process_dir $datasetDir > $datasetDir/index.php
for et in $(ls $datasetDir); do
	if [[ -d "$datasetDir/$et" ]]; then
		process_dir $datasetDir/$et > $datasetDir/$et/index.php
		for name in $(ls $datasetDir/$et); do
			if [[ -d "$datasetDir/$et/$name" ]]; then
				process_dataset $datasetDir $et $name > $datasetDir/$et/$name/index.php
			fi
		done
	fi
done