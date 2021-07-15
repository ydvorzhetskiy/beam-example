const a = {
    "xml": {                     // XML field validation
        "/transaction_id": {
            "emptyOrNullCheck": true,
            "minLengthCheck": 2,
            "maxLengthCheck": 10,
            "type": "int",
            "error": true       // false tells us that is only warning
        },
        "/transaction_id[value]": { // XML attribute validation
            "emptyOrNullCheck": true
        }
    },
    "json": {
        "$.fwd.OTA_AirLowFareSearchRQ": {
            "nonNull": true,
            error: true
        },
        "$.fwd": {
            "nonNull": true,
            error: true
        }
    },
    "csv": {
        containsHeader: true,
        nonEmptyColumns: [
            "Column name1",
            "Column name2"
        ]
    },
    "java": {               // Java field validation
        "transactionId": {
            "nonNull": true,
            "nonEmpty": true,
            "custom": [         // list of custom validators
                "com.sabre.airshopping.TickerNumberNonExists",
                "com.sabre.airshopping.TickerNumberPatternValidator"
            ]
        },
        "custom": [
            "com.sabre.airshopping.ClassValidator"
        ]
    },
    "invalidTable": "DLQ_BQ",
    "sendMetric": true
}
