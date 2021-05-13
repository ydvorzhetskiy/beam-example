#!/bin/sh

. ./vars.sh

gcloud scheduler jobs delete $JOB_NAME