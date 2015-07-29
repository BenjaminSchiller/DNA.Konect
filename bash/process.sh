#!/bin/bash

cd ..
for et in $(ls datasets); do
	for name in $(ls datasets/$et); do
		if [[ -d "datasets/$et/$name" ]]; then
			if [[ -e "datasets/$et/$name/out.$name" ]]; then
				if [[ ! -e "datasets/$et/$name/sorted.$name" ]]; then
					echo "creating $et - sorted.$name"
					src="datasets/$et/$name/out.$name"
					dst="datasets/$et/$name/sorted.$name"
					cat $src | grep -v '%' |  sort -g -k4,4 > $dst
				fi
			fi
		fi
	done
done