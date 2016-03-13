package com.trading

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import org.slf4j.LoggerFactory
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Unroll

import java.util.concurrent.TimeUnit

@Ignore
class LocalTradingOfficeSpecification extends Specification {

    static def log = LoggerFactory.getLogger(LocalTradingOfficeSpecification.class)

    def allocationReportId = UUID.randomUUID().toString()

    static def tradingOfficeApiClient = new RESTClient("http://localhost:5001/")

    @Unroll
    def "Trade with exchange mic as #micCode generates #confirmationType confirmation"(String micCode, String confirmationType) {
        given: "A new trade with FIXML representation"
        def fixmlAllocationMessage = String.format(fixmlAllocationMessage(), allocationReportId, micCode)
        log.info("Testing trade for " + micCode + ", expected to have " + confirmationType + " confirmation type.")
        log.info("Processing: " + allocationReportId)

        when: "We receive FIXML message describing allocation for a trade"

        tradingOfficeApiClient.post(
                path: "allocation-message-receiver/api/allocation",
                body: fixmlAllocationMessage,
                requestContentType: ContentType.TEXT
        )

        TimeUnit.SECONDS.sleep(10)

        then: "New confirmation is generated as PDF"

        def confirmation = tradingOfficeApiClient.get(path: "confirmation-service/api/confirmation/" + allocationReportId).responseData
        confirmation.content.size() > 250
        confirmation.allocationId == allocationReportId
        confirmation.confirmationType == confirmationType

        where:
        micCode | confirmationType
        "XNAS"  | "EMAIL"
        "XLON"  | "SWIFT"
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
