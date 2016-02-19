package com.trading;

import net.sf.jasperreports.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

class EmailConfirmationParser implements ConfirmationParser {

    private static final Logger LOG = LoggerFactory.getLogger(EmailConfirmationParser.class);

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("MMM d yyyy", Locale.US);

    private final JasperReport jasperReport;

    public EmailConfirmationParser() throws JRException {
        InputStream resourceAsStream = EmailConfirmationParser.class
                .getClassLoader().getResourceAsStream("Confirmation.jrxml");

        jasperReport = JasperCompileManager.compileReport(resourceAsStream);
    }

    @Override
    public Optional<Confirmation> parse(AllocationReport allocationReport) {

        try {
            byte[] data = JasperRunManager.runReportToPdf(
                    jasperReport, parameters(allocationReport), new JREmptyDataSource()
            );

            return Optional.of(
                    new Confirmation(allocationReport, data, Confirmation.EMAIL)
            );
        } catch (JRException e) {
            LOG.error(e.getMessage(), e);
        }

        return Optional.empty();
    }

    private static Map<String, Object> parameters(AllocationReport allocationReport) {
        Map<String, Object> map = new HashMap<>();
        map.put("ALLOC_RPT_ID", allocationReport.getAllocationId());
        map.put("TRANS_TYPE", "NEW");
        map.put("INST_ID_TYPE", "SEDOL");
        map.put("INST_ID", allocationReport.getSecurityId());

        Instrument instrument = allocationReport.getInstrument();
        map.put("ALLOC_INSTR_NAME", instrument.getName());
        map.put("CURRENCY", instrument.getCurrency());
        map.put("EXCHANGE", instrument.getExchange());

        map.put("QUANTITY", Integer.toString(allocationReport.getQuantity()));
        map.put("PRICE", allocationReport.getPrice().toString());
        map.put("SIDE", allocationReport.getTradeSide().toString());

        LocalDate tradeDate = LocalDate.parse(
                allocationReport.getTradeDate(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
        );

        map.put("TRADE_DATE", tradeDate.format(DATE_TIME_FORMATTER));

        return map;
    }
}
