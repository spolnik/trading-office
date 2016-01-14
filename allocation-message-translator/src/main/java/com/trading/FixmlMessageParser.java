package com.trading;

import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.javabean.Element;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

class FixmlMessageParser {
    public AllocationReport parse(String message) throws JDOMException, IOException, JaxenException {

        SAXBuilder saxBuilder = new SAXBuilder();

        StringReader stringReader = new StringReader(message);

        Document allocationMessage = saxBuilder.build(stringReader);
        Optional<Attribute> id = getElement(allocationMessage, "/FIXML/AllocRpt/@RptID");
        Optional<Attribute> transactionType = getElement(allocationMessage, "/FIXML/AllocRpt/@TransTyp");

        return new AllocationReport(
                id.get().getValue(),
                transactionType.get().getValue().equals("0") ? "BUY" : "SELL"
        );
    }

    private Optional<Attribute> getElement(Document allocationMessage, String path) throws JaxenException {
        XPath xpath = new JDOMXPath(path);
        return xpath.selectNodes(allocationMessage).stream().findFirst();
    }
}
