# Example Apache Beam Project

## Compile and run

To run it directly:

```shell
mvn clean install

# To run using DataflowRunner 
java -jar target/beam-example.jar --project=sabre-cdw-dev-sandbox --region=us-central1 --inputFile=gs://cdm-apache-beam-example/input/in.txt --dataset=beam_example --gcpTempLocation=gs://cdm-apache-beam-example/temp --stagingLocation=gs://cdm-apache-beam-example/staging --runner=org.apache.beam.runners.dataflow.DataflowRunner
```
