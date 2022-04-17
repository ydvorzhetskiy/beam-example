@echo off

call vars.bat

call java -cp ../target/beam-example.jar com.dxc.poc.beam.PubSubToBqApplication ^
    --project=sabre-cdw-dev-sandbox --region=us-central1 ^
    --appName=PubSubToBqApplication --jobName=ps-to-bq-job ^
    --inputTopic=projects/sabre-cdw-dev-sandbox/topics/%TOPIC_ID% ^
    --dataset=beam_example ^
    --gcpTempLocation=gs://cdm-apache-beam-example/temp ^
    --stagingLocation=gs://cdm-apache-beam-example/staging
