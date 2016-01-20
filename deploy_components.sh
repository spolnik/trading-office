#!/usr/bin/env bash
git checkout heroku-allocation-message-translator
git merge origin/master
git push

git checkout heroku-confirmation-sender
git merge origin/master
git push

git checkout heroku-confirmation-service
git merge origin/master
git push

git checkout heroku-instruments-service
git merge origin/master
git push

git checkout heroku-finance-data-service
git merge origin/master
git push

echo "DONE"