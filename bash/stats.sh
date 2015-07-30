#!/bin/bash

function stats {
	et=$1
	name=$2

	echo "- - - - - - - - - - - - - - - - - - - -"
	echo "- - - $et/$name"
	echo "- - - - - - - - - - - - - - - - - - - -"
	echo ""
	echo "Edge Type:  $et"
	echo "Name:       $name"
	echo ""

	line="$et;$name"

	readme="datasets/$et/$name/README.$name"
	if [[ -e $readme || -h $readme ]]; then
		title=$(head -1 $readme | cut -d ',' -f1)
		description=$(head -6 $readme | tail -1)
		url=$(head -10 $readme | tail -1)

		echo "Title:        $title"
		echo "URL: 	        $url"
		echo ""
		echo "Description:  $description"
		echo ""

		line="$line;$title;$url"
	else
		line="$line;;"
	fi

	html="datasets/$et/$name/$name.html"
	if [[ -e $html || -h $html ]]; then
		if [[ -n "$(cat $html | grep 'B.png')" ]]; then connection="B"
		elif [[ -n "$(cat $html | grep 'D.png')" ]]; then connection="D"
		elif [[ -n "$(cat $html | grep 'U.png')" ]]; then connection="U"
		else connection="unknown"
		fi

		echo "Connection:  $connection"
		echo ""

		line="$line;$connection"
	else
		line="$line;"
	fi

	file="datasets/$et/$name/sorted.$name"
	if [[ -e $file || -h $file ]]; then
		statistics=$(cat $file | awk '{if(min==""){min=max=$3}; if($3>max) {max=$3}; if($3<min) {min=$3}; total+=$3; count+=1} END {print count, min, max}')
		edges=$(echo $statistics | awk '{print $1}')
		minWeight=$(echo $statistics | awk '{print $2}')
		maxWeight=$(echo $statistics | awk '{print $3}')

		if [[ -d "/Applications" ]]; then
			# Mac
			filesize=$(stat -f%z "$file")
		else
			# Linux
			filesize=$(stat -c%s "$file")
		fi

		start=$(head -n 1 $file | awk '{print $4}' | tr '\n' ' ' | tr '\r' ' ' | xargs)
		end=$(tail -n 1 $file | awk '{print $4}' | tr '\n' ' ' | tr '\r' ' ' | xargs)
		minTimestamp=${start%%.*}
		maxTimestamp=${end%%.*}

		duration=$(echo "$maxTimestamp-$minTimestamp" | bc)
		durationPerEdge=$(echo "$duration/$edges" | bc)
		hours=$(echo "$duration/3600" | bc)
		days=$(echo "$duration/86400" | bc)
		weeks=$(echo "$duration/604800" | bc)
		years=$(echo "$duration/215308800" | bc)

		echo "Edges:       $edges"
		echo "File Size:   $filesize"
		echo "Min Weight:  $minWeight"
		echo "Max Weight:  $maxWeight"
		echo ""
		echo "Min Timestamp:       $minTimestamp"
		echo "Max Timestamp:       $maxTimestamp"
		echo "Duration:            $duration"
		echo "Duration per Edge:   $durationPerEdge"
		echo "Duration - Hours:    $hours"
		echo "Duration - Days:     $days"
		echo "Duration - Weeks:    $weeks"
		echo "Duration - Years:    $years"
		echo ""

		line="$line;$edges;$filesize;$minWeight;$maxWeight"
		line="$line;$minTimestamp;$maxTimestamp"
		line="$line;$duration;$durationPerEdge"
		line="$line;$hours;$days;$weeks;$years"
	else
		line="$line;;;;"
		line="$line;;"
		line="$line;;"
		line="$line;;;;"
	fi

	echo $line
}

cd ..

if [ "$#" -eq "2" ]; then
	stats $1 $2
else
	line="et;name"
	line="$line;title;url"
	line="$line;connection"
	line="$line;edges;filesize;minWeight;maxWeight"
	line="$line;minTimestamp;maxTimestamp"
	line="$line;duration;durationPerEdge"
	line="$line;hours;days;weeks;years"
	echo $line

	for et in $(ls datasets); do
		for name in $(ls datasets/$et); do
			if [[ ! -e "datasets/$et/$name/STATS.$name" ]]; then
				stats $et $name > datasets/$et/$name/STATS.$name
			fi
			cat datasets/$et/$name/STATS.$name
		done
	done
fi
