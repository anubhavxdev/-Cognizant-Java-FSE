package com.cognizant.composite.dto;

public class AccountDto {
    private String number;
    private String type;
    private double balance;

    public AccountDto() {}

    public AccountDto(String number, String type, double balance) {
        this.number = number;
        this.type = type;
        this.balance = balance;
    }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}
