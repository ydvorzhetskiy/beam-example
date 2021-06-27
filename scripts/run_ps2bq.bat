@echo off

call vars.bat

call java -cp ../target/beam-example.jar com.dxc.poc.beam.PubSubToBqApplication --jobName=pub_sub_to_bq --project=sabre-cdw-dev-sandbox --region=us-central1 --inputTopic=projects/sabre-cdw-dev-sandbox/topics/%TOPIC_ID% --dataset=beam_example --gcpTempLocation=gs://cdm-apache-beam-example/temp --stagingLocation=gs://cdm-apache-beam-example/staging
