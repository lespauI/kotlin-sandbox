#!/bin/bash

rm out.log

mvn clean package

java -jar ufl-bot/target/ufl-bot.jar -DPORT=2323 >> out.log &


curl -d '{"text":"\n-------Sanity Tests-------\n"}' -H "Content-Type: application/json" -X POST http://212.142.108.211:44000/daddyleague/-273770462

curl -d '{"text":"2 - The league has advanced to week.\n2023 - Season - Week 2\n49ers @ Redskins\nBills @ Eagles"}' -H "Content-Type: application/json" -X POST http://212.142.108.211:44000/daddyleague/-273770462
