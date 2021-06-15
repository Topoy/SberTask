package main;

import java.math.BigDecimal;

public class Person {
    private String name;
    private BigDecimal wallet;
    private BigDecimal appendFromBank;

    public Person(String name, BigDecimal wallet) {
        this.name = name;
        this.wallet = wallet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getWallet() {
        return wallet;
    }

    public void setWallet(BigDecimal wallet) {
        this.wallet = wallet;
    }

    public BigDecimal getAppendFromBank() {
        return appendFromBank;
    }

    public void setAppendFromBank(BigDecimal appendFromBank) {
        this.appendFromBank = appendFromBank;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", wallet=" + wallet +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Person guest = (Person) o;
        return this.name.equals(guest.name) && this.wallet.equals(guest.wallet);
    }


}
