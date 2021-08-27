@echo off

set WORKERS_NUM=3
set PROJECT_ID=nimble-furnace-320012
set GS_ROOT=artifacts.nimble-furnace-320012.appspot.com
set DATASET_NAME=example_set_new


call java -cp ./target/beam-example.jar com.dxc.poc.beam.BqToBqApplication --numWorkers=%WORKERS_NUM% --project=%PROJECT_ID%  --tempLocation=gs://%GS_ROOT%/temp --region=us-central1 --dataset=%DATASET_NAME% --gcpTempLocation=gs://%GS_ROOT%/temp --stagingLocation=gs://%GS_ROOT%/staging