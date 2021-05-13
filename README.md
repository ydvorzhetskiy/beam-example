# Example Apache Beam Project

## Compile and run

To run it directly:

```shell
mvn clean install

# To run using batch
java -jar target/beam-example.jar --project=sabre-cdw-dev-sandbox --region=us-central1 --inputFile=gs://cdm-apache-beam-example/input/in.txt --dataset=beam_example --gcpTempLocation=gs://cdm-apache-beam-example/temp --stagingLocation=gs://cdm-apache-beam-example/staging --runner=org.apache.beam.runners.dataflow.DataflowRunner

# To run using streaming
java -cp target/beam-example.jar com.dxc.poc.beam.PubSubToBqApplication --project=sabre-cdw-dev-sandbox --region=us-central1 --inputTopic=projects/sabre-cdw-dev-sandbox/topics/beam-example-topic --dataset=beam_example --gcpTempLocation=gs://cdm-apache-beam-example/temp --stagingLocation=gs://cdm-apache-beam-example/staging --runner=org.apache.beam.runners.dataflow.DataflowRunner
```
