#!/bin/bash
# https://github.com/0xNeffarion/osrsreboxed-db
# https://stackoverflow.com/questions/73870153/using-jq-to-filter-children-and-keep-only-specific-key

scriptPath="$(dirname "$0")"
inputFile="$scriptPath/osrsbox-enhanced-monsters/docs/monsters-complete.json"
outputFile="$scriptPath/monster_essentials.json"

cd $scriptPath/osrsbox-enchanced-monsters
git pull
cd ..

jq -c '.[] |= ({"id","name","max_hit","attack_type","attack_speed","attack_level","attack_stat":."attack_bonus",
"strength_level","strength_bonus","magic_level","magic_stat":."attack_magic","magic_bonus",
"ranged_level","ranged_stat":."attack_ranged","ranged_bonus"})' $inputFile > $outputFile
