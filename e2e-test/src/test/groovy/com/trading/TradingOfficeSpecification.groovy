package com.trading

import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.activemq.transport.InactivityIOException
import org.slf4j.LoggerFactory
import org.springframework.jms.connection.SingleConnectionFactory
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.core.MessageCreator
import org.springframework.web.client.RestTemplate
import spock.lang.Ignore
import spock.lang.Specification

import javax.jms.JMSException
import javax.jms.Message
import javax.jms.Session
import java.util.concurrent.TimeUnit

@Ignore
class TradingOfficeSpecification extends Specification {

    def log = LoggerFactory.getLogger(TradingOfficeSpecification.class)

    def allocationReportId = UUID.randomUUID().toString()

    def restTemplate = new RestTemplate()

    def setup() {
        healthCheck("http://allocation-message-translator.herokuapp.com/health")
        healthCheck("http://allocation-enricher.herokuapp.com/health")
        healthCheck("http://confirmation-sender.herokuapp.com/health")
    }

    def healthCheck(String url) {
        def status = restTemplate.getForObject(url, String.class)
        log.info(url + ": " + status)
    }

    def "For new trade we generate confirmation as pdf"() {
        given: "A new trade with FIXML representation"
        def fixmlAllocationMessage = String.format(fixmlAllocationMessage(), allocationReportId)

        when: "We receive FIXML message describing allocation for a trade"

        def jmsTemplate = new JmsTemplate(connectionFactory())

        jmsTemplate.send(
                queue(), messageCreator(fixmlAllocationMessage)
        )

        TimeUnit.SECONDS.sleep(10)

        then: "New confirmation is generated as PDF"

        try {
            assertConfirmation()
        } catch (InactivityIOException ex) {
            log.warn(ex.getMessage())
            log.warn("Retry")
            TimeUnit.SECONDS.sleep(5)
            assertConfirmation()
        }
    }

    private void assertConfirmation() {
        def confirmation = restTemplate.getForObject(
                "http://confirmation-service.herokuapp.com/api/confirmation?id=" + allocationReportId,
                Confirmation.class
        );

        confirmation.getContent().size() > 100
        confirmation.id() == allocationReportId
    }

    def messageCreator(fixmlAllocationMessage) {
        new MessageCreator() {

            @Override
            Message createMessage(Session session) throws JMSException {
                def message = session.createTextMessage(fixmlAllocationMessage)
                log.info("Sending message: " + message)
                message
            }
        }
    }

    def queue() {
        "front.office.mailbox"
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

        <Pty R="22" ID="IFEU"/>
        <Pty R="21" ID="ICEU"/>
        <Pty R="1" ID="GUF"/>
        <Pty R="12" ID="RYN"/>
        <Pty R="4" ID="GUC"/>
        <Pty R="24" ID="CU120978"/>
        <Pty R="38" ID="S">
            <Sub ID="1" Typ="26"/>
        </Pty>
        <Amt Typ="CRES"
             Amt="10.93" Ccy="EUR"/>
        <Alloc
                IndAllocID2="2827379"
                Qty="200">
            <Pty R="22" ID="IFEU"/>
            <Pty R="21" ID="ICEU"/>
            <Pty R="1" ID="TUF"/>
            <Pty R="4" ID="TCF"/>
            <Pty R="12" ID="TUF"/>
        </Alloc>
    </AllocRpt>
</FIXML>
"""
    }
}
