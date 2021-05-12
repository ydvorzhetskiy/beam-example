# Example Apache Beam Project

## Compile and run

To run it directly:

```shell
mvn clean install

# To run using DirectRunner
java -jar target/beam-example.jar --inputFile=./in.txt --outputFile=./out.txt --runner=DirectRunner

# To run using DataflowRunner 
java -jar target/beam-example.jar --project=sabre-cdw-dev-sandbox --region=us-central1 --inputFile=gs://cdm-apache-beam-example/input/in.txt --outputFile=gs://cdm-apache-beam-example/output/out.txt --gcpTempLocation=gs://cdm-apache-beam-example/temp --stagingLocation=gs://cdm-apache-beam-example/staging --runner=org.apache.beam.runners.dataflow.DataflowRunner 
```