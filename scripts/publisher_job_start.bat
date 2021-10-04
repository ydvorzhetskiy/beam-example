@echo off

call vars.bat

call gcloud scheduler jobs create pubsub beam-example-publisher-job ^
    --schedule="* * * * *" ^
    --topic=beam-example-topic ^
    --message-body-from-file=body.json

call gcloud scheduler jobs run beam-example-publisher-job