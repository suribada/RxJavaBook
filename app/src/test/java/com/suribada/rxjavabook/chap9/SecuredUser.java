package com.suribada.rxjavabook.chap9;

public class SecuredUser {

    private String name;
    private String securityNumber;
    private String tel;
    private int age;
    private String address;

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

    public static SecuredUser create(User user) {
        SecuredUser securedUser = new SecuredUser();
        securedUser.setName(user.getName());
        securedUser.setSecurityNumber("secured=" + user.getSecurityNumber());
        securedUser.setAge(user.getAge());
        securedUser.setTel(user.getTel());
        securedUser.setAddress("secured=" + user.getAddress());
        return securedUser;
    }
}
