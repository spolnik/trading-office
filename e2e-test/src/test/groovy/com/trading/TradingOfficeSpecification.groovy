package com.trading

import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.activemq.transport.InactivityIOException
import org.slf4j.LoggerFactory
import org.springframework.jms.connection.SingleConnectionFactory
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.core.MessageCreator
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import javax.jms.JMSException
import javax.jms.Message
import javax.jms.Session
import java.util.concurrent.TimeUnit

class TradingOfficeSpecification extends Specification {

    def log = LoggerFactory.getLogger(TradingOfficeSpecification.class)

    def allocationReportId = UUID.randomUUID().toString()

    def restTemplate = new RestTemplate()

    def setup() {
        healthCheck(herokuApp("allocation-message-translator"))
        healthCheck(herokuApp("allocation-enricher"))
        healthCheck(herokuApp("confirmation-sender"))
        healthCheck(herokuApp("confirmation-service"))
        healthCheck(herokuApp("financial-data-service"))
        healthCheck(herokuApp("instruments-service"))
        healthCheck(herokuApp("counterparty-service"))
    }

    def herokuApp(String name) {
        "http://" + name + ".herokuapp.com/health"
    }

    def healthCheck(String url) {
        log.info(url + ":")
        def status = restTemplate.getForObject(url, String.class)
        log.info(status)
    }

    def "For new trade we generate confirmation as pdf"(String micCode, ConfirmationType confirmationType) {
        given: "A new trade with FIXML representation"
        def fixmlAllocationMessage = String.format(fixmlAllocationMessage(), allocationReportId, micCode)

        when: "We receive FIXML message describing allocation for a trade"

        def jmsTemplate = new JmsTemplate(connectionFactory())

        jmsTemplate.send(
                Queues.INCOMING_FIXML_ALLOCATION_REPORT_QUEUE,
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
        "XNAS"  | ConfirmationType.EMAIL
        "XLON"  | ConfirmationType.SWIFT
    }

    def assertConfirmation(ConfirmationType confirmationType) {
        def confirmation = restTemplate.getForObject(
                "http://confirmation-service.herokuapp.com/api/confirmation?id=" + allocationReportId,
                Confirmation.class
        );

        log.info("Confirmation data: " + new String(confirmation.content))

        confirmation.getContent().size() > 100
        confirmation.id() == allocationReportId
        confirmation.getConfirmationType() == confirmationType
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
