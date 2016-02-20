package com.trading

import groovyx.net.http.RESTClient
import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.activemq.transport.InactivityIOException
import org.slf4j.LoggerFactory
import org.springframework.jms.connection.SingleConnectionFactory
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.core.MessageCreator
import spock.lang.Specification
import spock.lang.Unroll

import javax.jms.JMSException
import javax.jms.Message
import javax.jms.Session
import java.util.concurrent.TimeUnit

class TradingOfficeSpecification extends Specification {

    def log = LoggerFactory.getLogger(TradingOfficeSpecification.class)

    def allocationReportId = UUID.randomUUID().toString()

    def confirmationServiceClient = new RESTClient("http://confirmation-service.herokuapp.com/")

    def jmsTemplate = new JmsTemplate(connectionFactory())

    def setup() {
        healthCheck(herokuApp("allocation-message-receiver"))
        healthCheck(herokuApp("allocation-enricher"))
        healthCheck(herokuApp("confirmation-sender"))
        healthCheck(herokuApp("confirmation-service"))
        healthCheck(herokuApp("market-data-service"))
        healthCheck(herokuApp("counterparty-service"))
    }

    def herokuApp(String name) {
        "http://${name}.herokuapp.com/"
    }

    def healthCheck(String url) {

        def status = new RESTClient(url)
                .get(path: "health")
                .responseData.diskSpace.status.toString()

        log.info("$url - $status")
    }

    @Unroll
    def "For new trade with exchange mic as #micCode, we generate confirmation as #confirmationType"(String micCode, String confirmationType) {
        given: "A new trade with FIXML representation"
        def fixmlAllocationMessage = String.format(fixmlAllocationMessage(), allocationReportId, micCode)
        log.info("Processing: " + allocationReportId)

        when: "We receive FIXML message describing allocation for a trade"

        jmsTemplate.send(
                "incoming.fixml.allocation.report",
                messageCreator(fixmlAllocationMessage)
        )

        TimeUnit.SECONDS.sleep(10)

        then: "New confirmation is generated as PDF"

        try {
            assertConfirmation(confirmationType)
        } catch (InactivityIOException ex) {
            log.error(ex.getMessage())
            log.info("Retry")
            TimeUnit.SECONDS.sleep(5)
            assertConfirmation(confirmationType)
        }

        where:
        micCode | confirmationType
        "XNAS"  | "EMAIL"
        "XLON"  | "SWIFT"
    }

    def assertConfirmation(String confirmationType) {

        def confirmation = confirmationServiceClient.get(path: "api/confirmation/" + allocationReportId).responseData

        confirmation.content.size() > 100
        confirmation.allocationId == allocationReportId
        confirmation.confirmationType == confirmationType
    }

    def messageCreator(String fixmlAllocationMessage) {
        new MessageCreator() {

            @Override
            Message createMessage(Session session) throws JMSException {
                def message = session.createTextMessage(fixmlAllocationMessage)
                log.info("Sending message: " + message)
                message
            }
        }
    }

    def connectionFactory() {

        def activeMQConnectionFactory = new ActiveMQConnectionFactory()
        activeMQConnectionFactory.setBrokerURL("http://activemq-nprogramming.rhcloud.com")

        def factory = new SingleConnectionFactory()
        factory.setTargetConnectionFactory(activeMQConnectionFactory)
        factory
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
