#!/bin/bash

rsync -auvzl --prune-empty-dirs --include '*/' --include '*.png' --include 'README.*' --include '*.html' --exclude '*' p2pstorage:datasets/konect/datasets/ ../datasets/