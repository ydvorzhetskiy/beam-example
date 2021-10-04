@echo off

call vars.bat

call java -cp ../target/beam-example.jar com.dxc.poc.beam.PubSubToBqApplication ^
    --project=sab-dev-dap-common-4288 --region=us-central1 ^
    --appName=PubSubToBqApplication --jobName=ps-to-bq-job ^
    --inputTopic=projects/sab-dev-dap-common-4288/topics/%TOPIC_ID% ^
    --dataset=beam_example ^
    --subnetwork=https://www.googleapis.com/compute/v1/projects/sab-ssvcs-network-vpcs-5041/regions/us-central1/subnetworks/sn-dev-uscentral1-01^
    --usePublicIps=false^
    --gcpTempLocation=gs://cdm-apache-beam-example1/temp ^
    --stagingLocation=gs://cdm-apache-beam-example1/staging
