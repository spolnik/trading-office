#!/usr/bin/env bash

function refresh {

    git checkout $1
    git pull
    git merge master
    git push
}

git checkout master
git merge origin/master

refresh heroku-counterparty-service
refresh heroku-confirmation-service
refresh heroku-market-data-service
refresh heroku-allocation-enricher
refresh heroku-confirmation-sender
refresh heroku-eureka-server
refresh coverity_scan

git checkout master