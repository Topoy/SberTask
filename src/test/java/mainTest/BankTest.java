package mainTest;

import main.Bank;
import main.Person;
import main.XMLParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BankTest {

    private Bank sberBank;



    @BeforeEach
    public void setUp() {
        sberBank = new Bank();
        Document xmlDocument = XMLParser.buildXMLDocument("src/main/resources/BankWalletData.xml");
        Node firstNode = xmlDocument.getFirstChild();
        sberBank.setWallet(XMLParser.parseBankWallet(firstNode));

        NodeList nodeList = firstNode.getChildNodes();
        sberBank.setPersons(nodeList);
        ArrayList<Person> bankClients = sberBank.getPersons();


        try {
            sberBank.divideBankWallet(sberBank.getWallet(), bankClients);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        sberBank.setMinimalAppends(bankClients);
    }

    @Test
    void getWallet() {
        Assertions.assertEquals(BigDecimal.valueOf(3199.10).setScale(2, RoundingMode.DOWN),
                sberBank.getWallet());
    }

    @Test
    void setPersons() {

    }

    @Test
    void divideBankWallet() {
        ArrayList<Person> persons = new ArrayList<>();
        persons.add(new Person("Михаил", BigDecimal.valueOf(536.27)));
        persons.add(new Person("Татьяна", BigDecimal.valueOf(536.27)));
        persons.add(new Person("Игорь", BigDecimal.valueOf(536.27)));
        persons.add(new Person("Александр", BigDecimal.valueOf(536.27)));
        persons.add(new Person("Георг", BigDecimal.valueOf(536.26)));
        persons.add(new Person("Оля", BigDecimal.valueOf(536.26)));
        persons.add(new Person("Светлана", BigDecimal.valueOf(536.26)));
        org.assertj.core.api.Assertions.assertThat(sberBank.getPersons()).hasSameElementsAs(persons);
        //Assertions.assertEquals(persons.get(0).getName(), sberBank.getPersons().get(0).getName());


    }

    @Test
    void getMinimalAppends() {
    }

    @Test
    void setMinimalAppends() {
    }
}