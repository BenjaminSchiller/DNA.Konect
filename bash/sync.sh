#!/bin/Bash

cd ..

rsync -auvzl bash LICENSE README.md img p2pstorage:datasets/konect/
rsync -auvzl p2pstorage:datasets/konect/stats.* .