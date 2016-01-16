import org.apache.activemq.ActiveMQConnectionFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.jms.connection.SingleConnectionFactory
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.core.MessageCreator
import spock.lang.Ignore
import spock.lang.Specification

import javax.jms.JMSException
import javax.jms.Message
import javax.jms.Session

@Ignore
class TradingOfficeSpecification extends Specification {

    private static final Logger log = LoggerFactory.getLogger(TradingOfficeSpecification.class)

    def "For new trade we generate confirmation as pdf"() {
        given: "A new trade with FIXML representation"
        def fixmlAllocationMessage = fixmlAllocationMessage()

        when: "We receive FIXML message describing allocation for a trade"

        def jmsTemplate = new JmsTemplate(connectionFactory())
        jmsTemplate.send(
                queue(), messageCreator(fixmlAllocationMessage)
        )

        then: "New confirmation is generated as PDF"
        def confirmation = new File("../Confirmation.pdf")
        confirmation.size() > 0
        confirmation.delete()
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
        "front-office-mailbox"
    }

    def connectionFactory() {


        def activeMQConnectionFactory = new ActiveMQConnectionFactory()
        activeMQConnectionFactory.setBrokerURL("tcp://localhost:9999")

        def factory = new SingleConnectionFactory()
        factory.setTargetConnectionFactory(activeMQConnectionFactory)
        factory
    }

    def fixmlAllocationMessage() {
        """<FIXML>
    <AllocRpt
            TransTyp="0" RptID="1234567" GrpID="1234567" AvgPxGrpID="AP101" Stat="3" BizDt="2009-06-03" RptTyp="2"
            Qty="200" AvgPxInd="2"
            Side="1" TrdTyp="0"
            TrdSubTyp="5"
            AvgPx="57.5054673" TrdDt="2009-06-03" RndPx="57.51" GrpQty="350" RemQty="150"
            InptDev="API">
        <Hdr SID="ICE" TID="GUF"/>
        <Instrmt
                CFI="FXXXXX"
                SecTyp="FUT" Exch="IFEU" ID="B" MMY="200912"/>

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
