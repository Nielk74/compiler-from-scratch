#!/usr/bin/env bash


# This script update the coverage badge.
# The script assumes that the Jacoco report has been correctly generated in target/site/jacoco.svg

function pct () {
    echo "scale = $3; $1 * 100 / $2" | bc
}
passed=0
failed=0

while IFS="," read -r GROUP PACKAGE CLASS INSTRUCTION_MISSED INSTRUCTION_COVERED \
BRANCH_MISSED BRANCH_COVERED LINE_MISSED LINE_COVERED \
COMPLEXITY_MISSED COMPLEXITY_COVERED METHOD_MISSED METHOD_COVERED
do
  passed=$(($passed+$INSTRUCTION_COVERED))
  failed=$(($failed+$INSTRUCTION_MISSED))
done < <(tail -n +2 target/site/jacoco.csv)
coverage=$(pct $passed $((passed+failed)) 2)
if (( $(echo "$coverage < 75" |bc -l) )); then
  color="yellow"
else
  color="green"
fi
anybadge -l coverage -v "$coverage %" -f cover.svg -c $color
rm -rf /var/www/html/gitlab/*
cp -r target/site/ /var/www/html/gitlab/
