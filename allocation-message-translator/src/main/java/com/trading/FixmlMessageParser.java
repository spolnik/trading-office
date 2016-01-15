package com.trading;

import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

class FixmlMessageParser {

    private static final Logger log = LoggerFactory.getLogger(FixmlMessageParser.class);

    public AllocationReport parse(String message) throws JDOMException, IOException, JaxenException {

        SAXBuilder saxBuilder = new SAXBuilder();

        StringReader stringReader = new StringReader(message);

        Document allocationMessage = saxBuilder.build(stringReader);
        Optional<Attribute> id = getElement(allocationMessage, "/FIXML/AllocRpt/@RptID");
        Optional<Attribute> transactionType = getElement(allocationMessage, "/FIXML/AllocRpt/@TransTyp");

        AllocationReport allocationReport = new AllocationReport();
        allocationReport.setAllocationId(id.get().getValue());
        allocationReport.setTradeType(transactionType.get().getValue().equals("0") ? "BUY" : "SELL");

        log.info("Parsed: " + allocationReport);

        return allocationReport;
    }

    private Optional<Attribute> getElement(Document allocationMessage, String path) throws JaxenException {
        XPath xpath = new JDOMXPath(path);
        return xpath.selectNodes(allocationMessage).stream().findFirst();
    }
}
