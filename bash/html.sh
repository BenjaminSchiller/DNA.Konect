function img {
	for analysis in $(ls datasets/$et/$name/ | grep -v README); do
		file="datasets/$et/$name/$analysis/$1"
		if [[ -e $file ]]; then
			echo "<a href='$file'><img src='$file' alt='$2 for $name' title='$2 for $name' width='400'/></a>"
			break;
		fi
	done
}

cd ..

echo "<!DOCTYPE html><html><body>"

for et in $(ls datasets/); do
	echo "<h1>$et</h1>"
	for name in $(ls datasets/$et/); do
		a=$(ls datasets/$et/$name/ | grep -v README | wc -l | xargs)
		if [[ "$a" -lt "1" ]]; then continue; fi
		title=$(head -1 "datasets/$et/$name/README.$name" | cut -d ',' -f1)
		description=$(head -6 "datasets/$et/$name/README.$name" | tail -1)
		url=$(head -10 "datasets/$et/$name/README.$name" | tail -1)
		if [[ $description == *"undirected"* ]]; then
			img="U"
		elif [[ $description == *"directed"* ]]; then
			img="D"
		elif [[ $description == *"bipartite"* ]]; then
			img="B"
		elif [[ $name == *"sociopatterns"* ]]; then
			img="U"
		elif [[ $name == *"enron"* || $name == *"topology"* ]]; then
			img="D"
		else
			img=""
			echo "</br></br></br>unknown direction of the graph '$name'</br></br></br>"
		fi

		echo "<h2>$name - $title ($a) <img src='img/$img.png' title='$img' alt='$img'/> <img src='img/$et.png' title='$et' alt='$et'/></h2>"
		echo "<i>$description</i></br>"
		echo "<a href='$url'>$url</a></br>"
		img z.statistics.nodes.png 'node count'
		img z.statistics.updates.nodes.png 'node updates'
		img z.statistics.edges.png 'edge count'
		img z.statistics.updates.edges.png 'edge updates'
		echo "</br>"
		img DegreeDistributionR.degreeMin.png 'min degree'
		img DegreeDistributionR.degreeMax.png 'max degree'
		echo "</br>"
		img EdgeWeightsR-1.0.MinWeight.png 'min edge weight'
		img EdgeWeightsR-1.0.AverageWeight.png 'avg edge weight'
		img EdgeWeightsR-1.0.MaxWeight.png 'max edge weight'
		echo "</br>"
		img z.statistics.memory.png 'memory usage'
		img z.runtimes.0.all.CDF.png 'runtimes (CDF)'
		img z.runtimes.3.allMetrics.CDF.png 'metric runtimes (CDF)'
	done
done


echo "</body></html>"