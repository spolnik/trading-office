# Trading Office [![Build Status](https://travis-ci.org/spolnik/trading-office.svg?branch=master)](https://travis-ci.org/spolnik/trading-office) [![codecov.io](https://codecov.io/github/spolnik/trading-office/coverage.svg?branch=master)](https://codecov.io/github/spolnik/trading-office?branch=master) [![Sonar Coverage](https://img.shields.io/sonar/https/sonar-nprogramming.rhcloud.com/trading-office/coverage.svg)](https://sonar-nprogramming.rhcloud.com/dashboard/index/1) [![Sonar Tech Debt](https://img.shields.io/sonar/https/sonar-nprogramming.rhcloud.com/trading-office/tech_debt.svg)](https://sonar-nprogramming.rhcloud.com/dashboard/index/1) [![Coverity Scan Build Status](https://scan.coverity.com/projects/7604/badge.svg)](https://scan.coverity.com/projects/spolnik-trading-office)

Trading Office is reference implementation of microservices architecture, based on Spring Boot. It's modeling part of post trade processing, mainly focused on receiving Fixml message and preparing confirmation for it.

- [Introduction](#introduction)
- [Allocation Message Receiver](#allocation-message-receiver)
- [Allocation Enricher](#allocation-enricher)
- [Confirmation Sender](#confirmation-sender)
- [Instruments Service](#instruments-service)
- [Financial Data Service](#financial-data-service)
- [Confirmation Service](#confirmation-service)
- [Counterparty Service](#counterparty-service)
- [E2E Test](#e2e-test)
- [Infrastructure](#infrastructure)
- [Notes](#notes)

## Introduction

- set of applications simulating simple flow in post trade part of trade lifecycle
- it's focused on generating confirmation based on received allocation report

![Component Diagram](https://raw.githubusercontent.com/spolnik/trading-office/master/design/component_diagram.png)

## Allocation Message Receiver
- spring boot application
- subscribes to jms looking for new allocation report messages (fixml)
- after receiving message it parses it to AllocationReport POJO
- finally, it sends the POJO as json into ActiveMQ

Heroku: http://allocation-message-receiver.herokuapp.com/swagger-ui.html

![Component Diagram](https://raw.githubusercontent.com/spolnik/trading-office/master/design/allocation_message_receiver.png)

## Allocation Enricher
- spring boot application
- subscribes to jms looking for tranlated allocation report messages (json)
- after receiving message, it enriches it with instrument data (using Intrument Service, and Finance Data Service)
- finally, it sends enriched allocation as json into ActiveMQ

Heroku: http://allocation-enricher.herokuapp.com/health

![Component Diagram](https://raw.githubusercontent.com/spolnik/trading-office/master/design/allocation_enricher.png)

## Confirmation Sender
- spring boot application
- subscribes to jms looking for enriched allocation report messages (json)
- after receiving message, it generates PDF confirmation using JasperReports template
- finally, it sends the Confirmation POJO with attached PDF (as byte[]) to confirmation service (REST Service)

Heroku: http://confirmation-sender.herokuapp.com/health

![Component Diagram](https://raw.githubusercontent.com/spolnik/trading-office/master/design/confirmation_sender.png)

## Instruments Service
- spring boot web application
- exposes REST endpoints for instrument data
- works in readonly mode
- data consumed from [OpenFigi Api](https://openfigi.com/api)

Heroku: http://instruments-service.herokuapp.com/swagger-ui.html

![Component Diagram](https://raw.githubusercontent.com/spolnik/trading-office/master/design/instruments_service.png)

## Financial Data Service
- spring boot web application
- exposes REST endpoint for financial data (using Yahoo Finance Api)
- based on a given symbol, downloads instrument data with actual price
- works in readonly mode

Heroku: http://financial-data-service.herokuapp.com/swagger-ui.html

![Component Diagram](https://raw.githubusercontent.com/spolnik/trading-office/master/design/financial_data_service.png)

## Confirmation Service
- spring boot web application (rest service)
- exposes REST endpoint api to store and retrieve confirmations
- data stored as files

Heroku: http://confirmation-service.herokuapp.com/swagger-ui.html

![Component Diagram](https://raw.githubusercontent.com/spolnik/trading-office/master/design/confirmation_service.png)

## Counterparty Service
- spring boot web application (rest service)
- exposes REST endpoint to query Exchange data based on mic code
- exposes REST endpoint to query Party data based on custom id

Heroku: http://counterparty-service.herokuapp.com/swagger-ui.html

![Component Diagram](https://raw.githubusercontent.com/spolnik/trading-office/master/design/counterparty_service.png)

## E2E Test
- end to end tests written in spock
- it runs against deployed applications (Heroku, all above + OpenShift, ActiveMq)

=========

## Infrastructure
- Heroku (hosting microservices)
- Heroku Add-ons (logging - papertrial, monitoring - new relic)
- ActiveMQ (hosted on OpenShift)
- SonarQube (hosted on OpenShift) - https://sonar-nprogramming.rhcloud.com
- TravisCI - https://travis-ci.org/spolnik/trading-office
- Coverity (Static code analysis) - https://scan.coverity.com/projects/spolnik-trading-office

## Notes
- to have access to OpenShift activemq web console - run rhc port-forward activemq (only if you have admin access)
- checking if [dependencies are up to date](https://www.versioneye.com/user/projects/56ad39427e03c7003ba41427)
