#!/bin/bash

if [ "$#" == "1" ]; then
    echo "$(jq -C '' ./monster_essentials.json | grep -i -B 2 -A 16 "$1")"
else
    echo "You have to pass one name"
fi

exit

#jq -C '' ./monster_essentials.json | grep -i -A 16 "Tentacle"
