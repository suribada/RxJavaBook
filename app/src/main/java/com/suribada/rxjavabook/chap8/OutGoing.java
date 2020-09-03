package com.suribada.rxjavabook.chap8;

public class OutGoing {

    private int amount;

    public OutGoing(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "OutGoing{" +
                "amount=" + amount +
                '}';
    }

    public int getAmount() {
        return amount;
    }
}
