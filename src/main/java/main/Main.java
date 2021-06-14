package main;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.ArrayList;

public class Main
{
    public static void main(String[] args) {

        Bank sberBank = new Bank();
        Document xmlDocument = XMLParser.buildXMLDocument("src/main/resources/BankWalletData.xml");

        //получили кошелек банка
        Node firstNode = xmlDocument.getFirstChild();
        sberBank.setWallet(XMLParser.parseBankWallet(firstNode));

        //получили клиентов банка
        NodeList nodeList = firstNode.getChildNodes();
        sberBank.setPersons(nodeList);
        ArrayList<Person> bankClients = sberBank.getPersons();

        //распределили деньги по-ровну
        sberBank.divideBankWallet(sberBank.getWallet(), bankClients);
        for (Person person : bankClients) {
            System.out.println("Имя: " + person.getName() + "; Счет: " + person.getWallet() +
                    "; Получено от банка: " + person.getAppendFromBank());
        }

        //определили 3 клиентов с минимальным доходом от банка
        sberBank.setMinimalAppends(bankClients);
        for (String name : sberBank.getMinimalAppends()) {
            System.out.println(name);
        }
        for (Person person : bankClients) {
            System.out.println("Имя: " + person.getName() + "; Счет: " + person.getWallet() +
                    "; Получено от банка: " + person.getAppendFromBank());
        }
        XMLParser.createXML(bankClients, sberBank.getMinimalAppends());


    }
}
