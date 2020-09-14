#!/bin/bash

mvn clean package $2
heroku deploy:jar ufl-bot/target/ufl-bot.jar --app daddy-league-new$1

echo "Sanity tests" &
curl -d '{"text":"\n-------Sanity Tests-------\n"}' -H "Content-Type: application/json" -X POST localhost:8080/daddyleague/-273770462 &
curl -d '{"text":"1 - http://www.daddyleagues.com/uflrus/gamerecap/553823618"}' -H "Content-Type: application/json" -X POST localhost:8080/daddyleague/-273770462 &
curl -d '{"text":"2 - http://www.daddyleagues.com/uflrus/gamerecap/553823618"}' -H "Content-Type: application/json" -X POST localhost:8080/daddyleague/-273770462 &
curl -d '{"text":"3 - http://www.daddyleagues.com/uflrus/gamerecap/553823618"}' -H "Content-Type: application/json" -X POST localhost:8080/daddyleague/-273770462 &
curl -d '{"text":"4 - http://www.daddyleagues.com/uflrus/gamerecap/553823618"}' -H "Content-Type: application/json" -X POST localhost:8080/daddyleague/-273770462 &
curl -d '{"text":"2 - The league has advanced to week.\n2023 - Season - Week 2\n49ers @ Redskins\nBills @ Eagles"}' -H "Content-Type: application/json" -X POST localhost:8080/daddyleague/-273770462 &
curl -d '{"text":"3 - The Panthers Signed HB Christian McCaffrey\nhttp://daddyleagues.com/uflrus/player/57917730"}' -H "Content-Type: application/json" -X POST localhost:8080/daddyleague/-273770462 &
curl -d '{"text":"4 - The Panthers Released HB Christian McCaffrey\nhttp://daddyleagues.com/uflrus/player/57917730"}' -H "Content-Type: application/json" -X POST localhost:8080/daddyleague/-273770462 &
curl -X GET localhost:8080/daddyleague/weekgame/-273770462 &
curl -d '{"text":"6 - The Panthers Signed P Pantr\nhttp://daddyleagues.com/uflrus/player/57918056"}' -H "Content-Type: application/json" -X POST localhost:8080/daddyleague/-273770462 &
curl -d '{"text":"7 - The Panthers Released P Panter\nhttp://daddyleagues.com/uflrus/player/57918056"}' -H "Content-Type: application/json" -X POST localhost:8080/daddyleague/-273770462 &
curl -d '{"text":"8 - A new trade between San Francisco 49ers and Chicago Bears has been submitted for approval. http://www.daddyleagues.com/uflrus/frontoffice/trade/116"}' -H "Content-Type: application/json" -X POST localhost:8080/daddyleague/-273770462 &
curl -d '{"text":"9 - Trade between Cincinnati Bengals and Denver Broncos has been approved. http://daddyleagues.com/uflrus/frontoffice/trade/115"}' -H "Content-Type: application/json" -X POST localhost:8080/daddyleague/-273770462 &
curl -d '{"text":"10 - Trade between Cincinnati Bengals and Denver Broncos has been denied. http://daddyleagues.com/uflrus/frontoffice/trade/115"}' -H "Content-Type: application/json" -X POST localhost:8080/daddyleague/-273770462 &


heroku logs --tail -a daddy-league-new$1

