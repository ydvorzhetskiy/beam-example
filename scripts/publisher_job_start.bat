@echo off

call vars.bat

call gcloud scheduler jobs create pubsub %PUBLISHER_JOB_NAME% ^
    --schedule="* * * * *" ^
    --topic=%TOPIC_ID% ^
    --message-body-from-file=body.json

call gcloud scheduler jobs run %PUBLISHER_JOB_NAME%
