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
    public Optional<Confirmation> parse(Confirmation confirmation) {
        MT518 mt518 = new MT518();
        mt518.setSender("FOOSEDR0AXXX");
        mt518.setReceiver("FOORECV0XXXX");

        mt518.append(sequenceA());
        mt518.append(sequenceB(confirmation));

        byte[] data = mt518.message().getBytes(StandardCharsets.UTF_8);

        confirmation.setContent(data);
        confirmation.setConfirmationType(Confirmation.SWIFT);

        return Optional.of(confirmation);
    }

    private static MT518.SequenceB sequenceB(Confirmation confirmation) {

        return MT518.SequenceB.newInstance(
                tradeDate(confirmation).asTag(),
                price(confirmation).asTag(),
                Field22A.tag(buySellTag(confirmation)),
                currency(confirmation).asTag(),
                quantity(confirmation).asTag(),
                instrument(confirmation).asTag()
        );
    }

    private static Field35B instrument(Confirmation confirmation) {
        Field35B field35B = new Field35B();
        field35B.setDescription(confirmation.getInstrumentName());
        return field35B;
    }

    private static Field36B quantity(Confirmation confirmation) {
        Field36B f36b = new Field36B();
        f36b.setQualifier(MT518.CONF);
        f36b.setQuantityTypeCode(UNIT_NUMBER);
        f36b.setQuantity(Integer.toString(confirmation.getQuantity()));
        return f36b;
    }

    private static Field11A currency(Confirmation confirmation) {
        Field11A f11a = new Field11A();
        f11a.setCurrency(confirmation.getInstrumentCurrency());
        f11a.setQualifier(
                "BUY".equals(confirmation.getTradeSide()) ? MT518.FXIB : MT518.FXIS
        );

        return f11a;
    }

    private static String buySellTag(Confirmation confirmation) {
        return String.format(
                ":%s/%s",
                MT518.BUSE,
                "BUY".equals(confirmation.getTradeSide()) ? "BUYI" : "SELL"
        );
    }

    private static Field98A tradeDate(Confirmation confirmation) {
        Field98A f98a = new Field98A();

        LocalDate tradeDate = LocalDate.parse(
                confirmation.getTradeDate(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
        );

        f98a.setDate(tradeDate.format(formatter));
        f98a.setQualifier(MT518.TRAD);
        return f98a;
    }

    private static Field90A price(Confirmation confirmation) {
        Field90A f90a = new Field90A();
        f90a.setPrice(confirmation.getPrice().toString());
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
