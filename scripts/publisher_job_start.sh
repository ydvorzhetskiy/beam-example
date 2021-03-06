#!/bin/sh

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

. $DIR/vars.sh

gcloud scheduler jobs create pubsub $JOB_NAME --schedule="* * * * *" \
    --topic=$TOPIC_ID --message-body='{"pr_locator_id": "FGDNKU", "ticket_number": "1c345a7812u45", "pr_create_date": "2021-05-11", "pr_sequence": 1, "from_datetime": "2021-05-11 13:35:12", "tr_datetime": "2021-05-11 13:35:12", "creditcard": [{ "issuing_network": "Visa", "card_number": "4485764219977839"}]}'

gcloud scheduler jobs run $JOB_NAME
