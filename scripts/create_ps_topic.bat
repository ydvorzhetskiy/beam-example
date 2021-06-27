@echo off

call vars.bat

call gcloud pubsub topics create %TOPIC_ID%

