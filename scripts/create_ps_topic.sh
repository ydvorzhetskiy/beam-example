#!/bin/sh

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

. $DIR/vars.sh

gcloud pubsub topics create $TOPIC_ID

