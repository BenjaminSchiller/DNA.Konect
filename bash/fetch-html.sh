#!/bin/bash

cd ../datasets
for et in $(ls .); do
	cd $et
	for name in $(ls .); do
		cd $name
		if [[ ! -e "$name.html" ]]; then
			wget "http://konect.uni-koblenz.de/networks/$name.html"
		fi
		cd ..
	done
	cd ..
done
cd ..