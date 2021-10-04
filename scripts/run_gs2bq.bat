@echo off

call vars.bat

call java -jar ../target/beam-example.jar --project=sabre-cdw-dev-sandbox --region=us-central1 --jobName=gs-to-bq-job --inputFile=gs://cdm-apache-beam-example/input/in.err.txt --dataset=beam_example --gcpTempLocation=gs://cdm-apache-beam-example/temp --stagingLocation=gs://cdm-apache-beam-example/staging --subnetwork=https://www.googleapis.com/compute/v1/projects/sab-ssvcs-network-vpcs-5041/regions/us-central1/subnetworks/sn-dev-uscentral1-01^
 --usePublicIps=false^
 --serviceAccount=streaming-ingestion-001@sab-dev-dap-common-4288.iam.gserviceaccount.com^
