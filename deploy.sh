#!/bin/bash

mvn clean package $2
heroku deploy:jar dummy-web-service/target/dummy-web-service.jar --app daddy-league-new$1

echo "Sanity tests"
curl -d '{"text":"http://www.daddyleagues.com/uflrus/gamerecap/552968611"}' -H "Content-Type: application/json" -X POST https://daddy-league-new$1.herokuapp.com/daddyleague/-273770462
curl -d '{"text":"The league has advanced to week.\n2023 - Season - Week 2\n49ers @ Redskins\nBills @ Eagles"}' -H "Content-Type: application/json" -X POST https://daddy-league-new$1.herokuapp.com/daddyleague/-273770462
curl -d '{"text":"The Panthers Signed HB Christian McCaffrey\nhttp://daddyleagues.com/uflrus/player/45534568"}' -H "Content-Type: application/json" -X POST https://daddy-league-new$1.herokuapp.com/daddyleague/-273770462
curl -d '{"text":"The Panthers Released HB Christian McCaffrey\nhttp://daddyleagues.com/uflrus/player/45534568"}' -H "Content-Type: application/json" -X POST https://daddy-league-new$1.herokuapp.com/daddyleague/-273770462

heroku logs --tail -a daddy-league-new$1