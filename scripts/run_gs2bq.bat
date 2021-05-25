@echo off

call vars.bat

call java -jar ../target/beam-example.jar --project=sabre-cdw-dev-sandbox --region=us-central1 --jobName=gs-to-bq-job --inputFile=gs://cdm-apache-beam-example/input/in.txt --dataset=beam_example --gcpTempLocation=gs://cdm-apache-beam-example/temp --stagingLocation=gs://cdm-apache-beam-example/staging
