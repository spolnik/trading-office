[![Build Status](https://travis-ci.org/spolnik/trading-office.svg?branch=master)](https://travis-ci.org/spolnik/trading-office) [![codecov.io](https://codecov.io/github/spolnik/trading-office/coverage.svg?branch=master)](https://codecov.io/github/spolnik/trading-office?branch=master) [![Sonar Coverage](https://img.shields.io/sonar/https/sonar-nprogramming.rhcloud.com/trading-office/coverage.svg)](https://sonar-nprogramming.rhcloud.com/dashboard/index/1) [![Sonar Tech Debt](https://img.shields.io/sonar/https/sonar-nprogramming.rhcloud.com/trading-office/tech_debt.svg)](https://sonar-nprogramming.rhcloud.com/dashboard/index/1) [![Coverity Scan Build Status](https://scan.coverity.com/projects/7604/badge.svg)](https://scan.coverity.com/projects/spolnik-trading-office)

# Trading Office
- set of applications simulating simple flow in post trade part of trade lifecycle
- it's focused on generating confirmation based on received allocation report

## Allocation Message Translator
- spring boot application
- subscribes to jms looking for new allocation report messages (fixml)
- after receiving message it parses it to AllocationReport POJO
- finally, it sends the POJO as json into ActiveMQ
- deployment to heroku

## Allocation Enricher
- spring boot application
- subscribes to jms looking for tranlated allocation report messages (json)
- after receiving message, it enriches it with instrument data (using Intrument Service, and Finance Data Service)
- finally, it sends enriched allocation as json into ActiveMQ
- deployment to heroku

## Instrument Service
- spring boot web application
- exposes REST endpoints for instrument data
- works in readonly mode
- data consumed from instruments.json file
- deployment to heroku

## Finance Data Service
- spring boot web application
- exposes REST endpoint for financial data (using Yahoo Finance Api)
- based on a given symbol, downloads instrument data with actual price
- works in readonly mode
- deployment to heroku

## Confirmation Sender
- spring boot application
- subscribes to jms looking for enriched allocation report messages (json)
- after receiving message, it generates PDF confirmation using JasperReports template
- finally, it sends the Confirmation POJO with attached PDF (as byte[]) to confirmation service (REST Service)
- deployment to heroku

## Confirmation Service
- spring boot web application (rest service)
- exposes REST endpoint api to store and retrieve confirmations
- data stored as files
- deployment to heroku

## Trading Domain
- library, containing all domain specific entities

## E2E Test
- end to end tests written in spock
- it runs against deployed applications (Heroku, all above + OpenShift, ActiveMq)

## ActiveMQ
- OpenShift hosted ActiveMQ 5.13

=========

# Technologies used

## Infrastructure
- Heroku
- Openshift
- Heroku Add-ins (logging, monitoring)
- TravisCI
- SonarQube (hosted on openshift)
- Coverity (Static code analysis)

#Notes
- to have access to OpenShift activemq web console - run rhc port-forward activemq (only if you have admin access)
