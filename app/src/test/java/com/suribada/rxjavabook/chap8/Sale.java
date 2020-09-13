package com.suribada.rxjavabook.chap8;

public class Sale implements Comparable<Sale> {
    private int price;

    public Sale(int value) {
        this.price = value;
    }

    public int getPrice() {
        return price;
    }

    public static Sale create(int price) {
        return new Sale(price);
    }

    @Override
    public String toString() {
        return "Sale{" +
                "price=" + price +
                '}';
    }

    @Override
    public int compareTo(Sale o) {
        if (this.getPrice() == o.getPrice()) {
            return 0;
        }
        if (this.getPrice() > o.getPrice()) {
            return 1;
        }
        return -1;
    }
}
