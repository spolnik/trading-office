package com.trading;

import com.prowidesoftware.swift.model.field.*;
import com.prowidesoftware.swift.model.mt.mt5xx.MT518;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import static com.trading.DomainObjectMapper.objectMapper;

@Component
public class SwiftConfirmationMessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(SwiftConfirmationMessageListener.class);

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String UNIT_NUMBER = "UNIT";

    private final Sender<Confirmation> confirmationSender;

    @Autowired
    public SwiftConfirmationMessageListener(Sender<Confirmation> confirmationSender) {
        this.confirmationSender = confirmationSender;
    }

    @JmsListener(destination = Queues.ENRICHED_JSON_ALLOCATION_REPORT_SWIFT_QUEUE, containerFactory = "jmsContainerFactory")
    public void processEnrichedAllocationReport(String message) throws IOException {
        AllocationReport allocationReport = objectMapper().toAllocationReport(message);
        LOG.info("Received: " + allocationReport);

        MT518 confirmation = new MT518();
        confirmation.setSender("FOOSEDR0AXXX");
        confirmation.setReceiver("FOORECV0XXXX");

        confirmation.append(sequenceA());
        confirmation.append(sequenceB(allocationReport));

        byte[] data = confirmation.message().getBytes();

        confirmationSender.send(
                new Confirmation(allocationReport, data, ConfirmationType.SWIFT)
        );
    }

    private static MT518.SequenceB sequenceB(AllocationReport allocationReport) {

        return MT518.SequenceB.newInstance(
                tradeDate(allocationReport).asTag(),
                price(allocationReport).asTag(),
                Field22A.tag(buySellTag(allocationReport)),
                currency(allocationReport).asTag(),
                quantity(allocationReport).asTag(),
                instrument(allocationReport).asTag()
        );
    }

    private static Field35B instrument(AllocationReport allocationReport) {
        Field35B field35B = new Field35B();
        field35B.setDescription(allocationReport.getInstrument().getName());
        return field35B;
    }

    private static Field36B quantity(AllocationReport allocationReport) {
        Field36B f36b = new Field36B();
        f36b.setQualifier(MT518.CONF);
        f36b.setQuantityTypeCode(UNIT_NUMBER);
        f36b.setQuantity(Integer.toString(allocationReport.getQuantity()));
        return f36b;
    }

    private static Field11A currency(AllocationReport allocationReport) {
        Field11A f11a = new Field11A();
        f11a.setCurrency(allocationReport.getInstrument().getCurrency());
        f11a.setQualifier(
                allocationReport.getTradeSide() == TradeSide.BUY ? MT518.FXIB : MT518.FXIS
        );

        return f11a;
    }

    private static String buySellTag(AllocationReport allocationReport) {
        return String.format(
                ":%s/%s",
                MT518.BUSE,
                allocationReport.getTradeSide() == TradeSide.BUY ? "BUYI" : "SELL"
        );
    }

    private static Field98A tradeDate(AllocationReport allocationReport) {
        Field98A f98a = new Field98A();
        f98a.setDate(allocationReport.getTradeDate().format(formatter));
        f98a.setQualifier(MT518.TRAD);
        return f98a;
    }

    private static Field90A price(AllocationReport allocationReport) {
        Field90A f90a = new Field90A();
        f90a.setPrice(allocationReport.getPrice().toString());
        return f90a;
    }

    private static MT518.SequenceA sequenceA() {
        return MT518.SequenceA.newInstance(
                Field20C.tag(":SEME//2005071800000923"),
                Field23G.tag("NEWM"),
                Field22F.tag(String.format(":TRTR//%s", MT518.TRAD))
        );
    }
}
