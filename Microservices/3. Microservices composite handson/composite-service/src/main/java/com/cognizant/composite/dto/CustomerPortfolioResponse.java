package com.cognizant.composite.dto;

import java.time.LocalDateTime;

public class CustomerPortfolioResponse {
    private AccountDto account;
    private LoanDto loan;
    private String status;
    private LocalDateTime timestamp;

    public CustomerPortfolioResponse() {}

    public CustomerPortfolioResponse(AccountDto account, LoanDto loan, String status) {
        this.account = account;
        this.loan = loan;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public AccountDto getAccount() { return account; }
    public void setAccount(AccountDto account) { this.account = account; }

    public LoanDto getLoan() { return loan; }
    public void setLoan(LoanDto loan) { this.loan = loan; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
