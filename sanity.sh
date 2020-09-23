#!/bin/bash


echo "Sanity tests"
curl -d '{"text":"\n-------Sanity Tests-------\n"}' -H "Content-Type: application/json" -X POST $1/daddyleague/-273770462
curl -d '{"text":"1 - http://www.daddyleagues.com/uflrus/gamerecap/553860968"}' -H "Content-Type: application/json" -X POST $1/daddyleague/-273770462
curl -d '{"text":"2 - The league has advanced to week.\n2023 - Season - Week 2\n49ers @ Redskins\nBills @ Eagles"}' -H "Content-Type: application/json" -X POST $1/daddyleague/-273770462
curl -d '{"text":"3 - The Panthers Signed HB Christian McCaffrey\nhttp://daddyleagues.com/uflrus/player/58734065"}' -H "Content-Type: application/json" -X POST $1/daddyleague/-273770462
curl -d '{"text":"4 - The Panthers Released HB Christian McCaffrey\nhttp://daddyleagues.com/uflrus/player/58734065"}' -H "Content-Type: application/json" -X POST $1/daddyleague/-273770462
curl -X GET $1/daddyleague/weekgame/-273770462

curl -d '{"text":"6 - The Panthers Signed P Pantr\nhttp://daddyleagues.com/uflrus/player/58734799"}' -H "Content-Type: application/json" -X POST $1/daddyleague/-273770462
curl -d '{"text":"7 - The Panthers Released P Panter\nhttp://daddyleagues.com/uflrus/player/58734799"}' -H "Content-Type: application/json" -X POST $1/daddyleague/-273770462

