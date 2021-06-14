package main;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Bank {
    private BigDecimal wallet;
    private final ArrayList<Person> persons = new ArrayList<>();
    private final ArrayList<BigDecimal> initialPersonWallets = new ArrayList<>();
    private final ArrayList<String> minimalAppends = new ArrayList<>();


    public BigDecimal getWallet() {
        return wallet;
    }

    public void setWallet(BigDecimal wallet) {
        this.wallet = wallet;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String bankClientName = XMLParser.getAttributeByName(nodeList.item(i), "name");
            String bankClientWalletValue = XMLParser.getAttributeByName(nodeList.item(i), "wallet");
            this.persons.add(new Person(bankClientName, new BigDecimal(bankClientWalletValue)));
            initialPersonWallets.add(new BigDecimal(bankClientWalletValue));
        }
    }

    public void divideBankWallet(BigDecimal bankWallet, ArrayList<Person> persons) throws Exception {
        if (!walletIsPositive(bankWallet, persons)) {
            throw new Exception("Wallet is negative");
        }
        int personsCount = persons.size();
        BigDecimal totalAmount = getPersonsTotalWallet(persons).add(bankWallet);
        BigDecimal fraction = totalAmount.divide(BigDecimal.valueOf(personsCount), RoundingMode.DOWN);

        ArrayList<Person> poorPersons = createPoorPersons(fraction, persons);
        int poorPersonsCount = poorPersons.size();
        if (poorPersons.size() != persons.size()) {
            totalAmount = getPersonsTotalWallet(poorPersons).add(bankWallet);
            fraction = totalAmount.divide(BigDecimal.valueOf(poorPersonsCount), RoundingMode.DOWN);
        }

        BigDecimal preliminaryTotalAmount = fraction.multiply(BigDecimal.valueOf(poorPersonsCount));
        BigDecimal reminder = totalAmount.subtract(preliminaryTotalAmount);

        distributeMoney(reminder, fraction, poorPersons);
        setAppendFromBankForClients(persons, initialPersonWallets);
        System.out.println("Общая сумма: " + totalAmount + "; На одного: " + fraction +
                "; На всех: " + preliminaryTotalAmount + "; Остаток: " + reminder);
    }

    public ArrayList<String> getMinimalAppends() {
        return minimalAppends;
    }

    public void setMinimalAppends(ArrayList<Person> persons) {
        ArrayList<Person> temporaryList = new ArrayList<>(persons);
        Collections.sort(temporaryList, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getAppendFromBank().compareTo(o2.getAppendFromBank());
            }
        });
        for (int i = 0; i < 3; i++) {
            this.minimalAppends.add(temporaryList.get(i).getName());
        }
    }


    private void setAppendFromBankForClients(ArrayList<Person> persons, ArrayList<BigDecimal> initialPersonWallets) {
        BigDecimal append;
        int count = 0;
        for (Person person : persons) {
            append = person.getWallet().subtract(initialPersonWallets.get(count));
            person.setAppendFromBank(append);
            count++;
        }
    }

    private BigDecimal getPersonsTotalWallet(ArrayList<Person> persons) {
        BigDecimal amount = new BigDecimal(0);
        for (Person person : persons) {
            amount = amount.add(person.getWallet());
        }
        return amount;
    }

    private boolean walletIsPositive(BigDecimal bankWallet, ArrayList<Person> persons) {
        if (bankWallet.compareTo(BigDecimal.ZERO) < 0) {
            return false;
        } else {
            for (Person person : persons) {
                if (person.getWallet().compareTo(BigDecimal.ZERO) < 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private ArrayList<Person> createPoorPersons(BigDecimal fraction, ArrayList<Person> persons) {
        ArrayList<Person> poorPersons = new ArrayList<>();
        for (Person person : persons) {
            if (person.getWallet().compareTo(fraction) <= 0) {
                poorPersons.add(person);
            }
        }
        return poorPersons;
    }

    private void distributeMoney(BigDecimal moneyReminder, BigDecimal fraction, ArrayList<Person> persons) {
        BigDecimal reminderFraction = BigDecimal.valueOf(0.01);
        for (Person person : persons) {
            if (moneyReminder.doubleValue() != 0) {
                person.setWallet(fraction.add(reminderFraction));
                moneyReminder = moneyReminder.subtract(reminderFraction);
                continue;
            }
            person.setWallet(fraction);
        }
    }
}
