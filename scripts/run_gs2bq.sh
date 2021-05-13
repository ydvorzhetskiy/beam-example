#!/bin/sh

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

. $DIR/vars.sh

java -jar target/beam-example.jar --project=sabre-cdw-dev-sandbox --region=us-central1 --inputFile=gs://cdm-apache-beam-example/input/in.txt --dataset=beam_example --gcpTempLocation=gs://cdm-apache-beam-example/temp --stagingLocation=gs://cdm-apache-beam-example/staging