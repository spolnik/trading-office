# Trading Office [![Build Status](https://travis-ci.org/spolnik/trading-office.svg?branch=master)](https://travis-ci.org/spolnik/trading-office) [![codecov.io](https://codecov.io/github/spolnik/trading-office/coverage.svg?branch=master)](https://codecov.io/github/spolnik/trading-office?branch=master) [![Sonar Coverage](https://img.shields.io/sonar/https/sonar-nprogramming.rhcloud.com/trading-office/coverage.svg)](https://sonar-nprogramming.rhcloud.com/dashboard/index/1) [![Sonar Tech Debt](https://img.shields.io/sonar/https/sonar-nprogramming.rhcloud.com/trading-office/tech_debt.svg)](https://sonar-nprogramming.rhcloud.com/dashboard/index/1) [![Coverity Scan Build Status](https://scan.coverity.com/projects/7604/badge.svg)](https://scan.coverity.com/projects/spolnik-trading-office)

Trading Office is reference implementation of microservices architecture, based on Spring Boot. It's modeling part of post trade processing, mainly focused on receiving Fixml message and preparing confirmation for it.

- [Introduction](#introduction)
- [Allocation Message Receiver](#allocation-message-receiver)
- [Allocation Enricher](#allocation-enricher)
- [Confirmation Sender](#confirmation-sender)
- [Market Data Service](#market-data-service)
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
- [Allocation Message Receiver](https://github.com/spolnik/trading-office-allocation-message-receiver)

## Allocation Enricher
- [Allocation Enricher](https://github.com/spolnik/trading-office-allocation-enricher)

## Confirmation Sender
- [Confirmation Sender](https://github.com/spolnik/trading-office-confirmation-sender)

## Market Data Service
- spring boot web application
- exposes REST endpoint for market data (using Yahoo Finance Api)
- exposes REST endpoints for instrument data (data consumed from [OpenFigi Api](https://openfigi.com/api))
- based on a given symbol, downloads instrument data with actual price
- works in readonly mode

Heroku: http://market-data-service.herokuapp.com/swagger-ui.html

![Component Diagram](https://raw.githubusercontent.com/spolnik/trading-office/master/design/market_data_service.png)

## Confirmation Service
- spring boot web application (rest service)
- exposes REST endpoint api to store and retrieve confirmations
- confirmations stored as files

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
- it runs against deployed applications (Heroku)

=========

## Infrastructure
- Heroku (hosting microservices)
- Heroku Add-ons (logging - papertrial, monitoring - new relic)
- RabbitMQ (CloudAMQP hosted on heroku)
- SonarQube (hosted on OpenShift) - https://sonar-nprogramming.rhcloud.com
- TravisCI - https://travis-ci.org/spolnik/trading-office
- Coverity (Static code analysis) - https://scan.coverity.com/projects/spolnik-trading-office

## Domain

- Swift - http://www.iso15022.org/uhb/uhb/finmt518.htm
- FIXML - http://btobits.com/fixopaedia/fixdic50-sp2-ep/index.html (Allocation Report message)
- Trade Lifecycle - http://thisweekfinance.blogspot.com/2011/10/trade-life-cycle.html

![Trade Lifecycle](https://raw.githubusercontent.com/spolnik/trading-office/master/design/trade_lifecycle.jpg)

## Notes
- checking if [dependencies are up to date](https://www.versioneye.com/user/projects/56ad39427e03c7003ba41427)
- installing RabbitMQ locally (to run end to end test locally) - [instructions](https://www.rabbitmq.com/download.html)
- to run on Mac OS X - /usr/local/sbin/rabbitmq-server 
