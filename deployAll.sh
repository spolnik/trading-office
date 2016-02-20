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
refresh heroku-financial-data-service
refresh heroku-allocation-message-receiver
refresh heroku-allocation-enricher
refresh heroku-confirmation-sender
refresh coverity_scan

git checkout master