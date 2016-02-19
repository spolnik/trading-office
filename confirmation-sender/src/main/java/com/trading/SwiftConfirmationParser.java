package com.trading;

import com.prowidesoftware.swift.model.field.*;
import com.prowidesoftware.swift.model.mt.mt5xx.MT518;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class SwiftConfirmationParser implements ConfirmationParser {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String UNIT_NUMBER = "UNIT";

    @Override
    public Optional<Confirmation> parse(AllocationReport allocationReport) {
        MT518 confirmation = new MT518();
        confirmation.setSender("FOOSEDR0AXXX");
        confirmation.setReceiver("FOORECV0XXXX");

        confirmation.append(sequenceA());
        confirmation.append(sequenceB(allocationReport));

        byte[] data = confirmation.message().getBytes(StandardCharsets.UTF_8);

        return Optional.of(new Confirmation(allocationReport, data, Confirmation.SWIFT));
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

        LocalDate tradeDate = LocalDate.parse(
                allocationReport.getTradeDate(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
        );

        f98a.setDate(tradeDate.format(formatter));
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
