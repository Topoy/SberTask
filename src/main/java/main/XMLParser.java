package main;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;

public class XMLParser {
    public static Document buildXMLDocument(String path) {
        File xmlFile = new File(path);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document xmlDocument = null;

        try {
            xmlDocument = dbf.newDocumentBuilder().parse(xmlFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlDocument;
    }

    public static BigDecimal parseBankWallet(Node node) {
        String value = node.getAttributes().getNamedItem("wallet").getFirstChild().getTextContent();
        return new BigDecimal(value);
    }

    public static String getAttributeByName(Node node, String name) {
        return node.getAttributes().getNamedItem(name).getTextContent();
    }

    public static void createXML(ArrayList<Person> persons, ArrayList<String> minimalAppends) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element rootElement = doc.createElement("total");
            Element resultElement = doc.createElement("result");
            Element minimalElement = doc.createElement("minimal");

            doc.appendChild(rootElement);
            rootElement.appendChild(resultElement);
            rootElement.appendChild(minimalElement);

            for (Person person : persons) {
                Element personElement = doc.createElement("Person");
                personElement.setAttribute("name", person.getName());
                personElement.setAttribute("wallet", person.getWallet() + "");
                personElement.setAttribute("appendFromBank", person.getAppendFromBank() + "");
                resultElement.appendChild(personElement);
            }

            for (String name : minimalAppends) {
                Element personElement = doc.createElement("Person");
                personElement.setAttribute("name", name);
                minimalElement.appendChild(personElement);
            }


            StreamResult file = new StreamResult(new File("src/main/resources/result.xml"));
            DOMSource source = new DOMSource(doc);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //transformer.setOutputProperty(OutputKeys.);
            transformer.transform(source, file);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
