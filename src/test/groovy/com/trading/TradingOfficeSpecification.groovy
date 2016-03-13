package com.trading

import groovyx.gpars.GParsPool
import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import org.slf4j.LoggerFactory
import spock.lang.Specification
import spock.lang.Unroll

import java.util.concurrent.TimeUnit

class TradingOfficeSpecification extends Specification {

    static def log = LoggerFactory.getLogger(TradingOfficeSpecification.class)

    def allocationReportId = UUID.randomUUID().toString()

    static def tradingOfficeApiClient = new RESTClient("http://trading-office-api.herokuapp.com/")
    static def tradingOfficeApiStagingClient = new RESTClient("http://trading-office-api-staging.herokuapp.com/")

    def setupSpec() {

        def services = [
                new Tuple2<String, String>('eureka-server-staging', 'eureka-server'),
                new Tuple2<String, String>('confirmation-service-staging', 'confirmation-service'),
                new Tuple2<String, String>('market-data-service-staging', 'market-data-service'),
                new Tuple2<String, String>('counterparty-service-staging', 'counterparty-service'),
                new Tuple2<String, String>('trading-office-api-staging', 'trading-office-api'),
                new Tuple2<String, String>('allocation-receiver-staging', 'allocation-message-receiver'),
                new Tuple2<String, String>('allocation-enricher-staging', 'allocation-enricher'),
                new Tuple2<String, String>('confirmation-sender-staging', 'confirmation-sender')
        ];

        services.each {
            healthCheck("http://${it.getFirst()}.herokuapp.com/" );
            healthCheck("http://${it.getSecond()}.herokuapp.com/" );
        }

    }

    def healthCheck(String url) {

        log.info("$url - checking...")
        def status = new RESTClient(url)
                .get(path: "health")
                .responseData.diskSpace.status.toString()

        log.info("$url - $status")
    }

    @Unroll
    def "[#env] Trade with exchange mic as #micCode generates #confirmationType confirmation"(String micCode, String confirmationType, client, String env) {
        given: "A new trade with FIXML representation"
        def fixmlAllocationMessage = String.format(fixmlAllocationMessage(), allocationReportId, micCode)
        log.info("[" + env + "] Testing trade for " + micCode + ", expected to have " + confirmationType + " confirmation type.")
        log.info("Processing: " + allocationReportId)

        when: "We receive FIXML message describing allocation for a trade"

        client.post(
                path: "allocation-message-receiver/api/allocation",
                body: fixmlAllocationMessage,
                requestContentType: ContentType.TEXT
        )

        TimeUnit.SECONDS.sleep(10)

        then: "New confirmation is generated as PDF"

        def confirmation = client.get(path: "confirmation-service/api/confirmation/" + allocationReportId).responseData
        confirmation.content.size() > 250
        confirmation.allocationId == allocationReportId
        confirmation.confirmationType == confirmationType

        where:
        micCode | confirmationType  | client                        | env
        "XNAS"  | "EMAIL"           | tradingOfficeApiClient        | "PROD"
        "XLON"  | "SWIFT"           | tradingOfficeApiClient        | "PROD"
        "XNAS"  | "EMAIL"           | tradingOfficeApiStagingClient | "STAGING"
        "XLON"  | "SWIFT"           | tradingOfficeApiStagingClient | "STAGING"
    }

    def fixmlAllocationMessage() {
        """<FIXML>
    <AllocRpt
            TransTyp="0" RptID="%s" GrpID="1234567" AvgPxGrpID="AP101" Stat="3" BizDt="2016-06-03" RptTyp="2"
            Qty="200" AvgPxInd="2"
            Side="1" TrdTyp="0"
            TrdSubTyp="5"
            AvgPx="57.5054673" TrdDt="2016-06-03" RndPx="57.51" GrpQty="350" RemQty="150"
            InptDev="API">
        <Hdr SID="ICE" TID="GUF"/>
        <Instrmt ID="2000019" Src="2"/>

        <Pty R="1" Src="D" ID="TROF" />
        <Pty R="22" Src="G" ID="%s" />
        <Pty R="3" Src="D" ID="CUSTUS" />
        <Amt Typ="CRES"
             Amt="10.93" Ccy="EUR"/>
        <Alloc
                IndAllocID2="2827379"
                Qty="200">
        </Alloc>
    </AllocRpt>
</FIXML>
"""
    }
}
