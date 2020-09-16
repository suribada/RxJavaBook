package com.suribada.rxjavabook.chap9;

public class User {

    private String name;
    private String securityNumber;
    private String tel;
    private int age;
    private String address;

    public User(String name, String securityNumber, String tel, int age, String address) {
        this.name = name;
        this.securityNumber = securityNumber;
        this.tel = tel;
        this.age = age;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecurityNumber() {
        return securityNumber;
    }

    public void setSecurityNumber(String securityNumber) {
        this.securityNumber = securityNumber;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isInvalid() {
        return (age == 24);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", securityNumber='" + securityNumber + '\'' +
                ", tel='" + tel + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                '}';
    }
}
