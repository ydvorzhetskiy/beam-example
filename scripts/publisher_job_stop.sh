#!/bin/sh

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

. $DIR/vars.sh

gcloud scheduler jobs delete $JOB_NAME