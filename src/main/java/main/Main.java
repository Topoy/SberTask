package main;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class Main {

    public static Bank sberbank;

    public static void main(String[] args) {

        sberbank = new Bank();
        Document xmlDocument = XMLParser.buildXMLDocument(args[0]);

        //получили кошелек банка
        Node firstNode = xmlDocument.getFirstChild();
        sberbank.setWallet(XMLParser.parseBankWallet(firstNode));

        //получили клиентов банка
        NodeList nodeList = firstNode.getChildNodes();
        XMLParser.personsParsing(sberbank, nodeList);
        ArrayList<Person> bankClients = sberbank.getPersons();

        //распределили деньги по-ровну
        try {
            sberbank.divideBankWallet(sberbank.getWallet(), bankClients);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //определили 3 клиентов с минимальным доходом от банка
        sberbank.setMinimalAppends(bankClients);
        for (String name : sberbank.getMinimalAppends()) {
            System.out.println(name);
        }
        for (Person person : bankClients) {
            System.out.println("Имя: " + person.getName() + "; Счет: " + person.getWallet() +
                    "; Получено от банка: " + person.getAppendFromBank());
        }

        //Создали XML с результатом
        XMLParser.createXML(bankClients, sberbank.getMinimalAppends(), args[1]);
    }

    public static Bank getBank() {
        return sberbank;
    }

}
