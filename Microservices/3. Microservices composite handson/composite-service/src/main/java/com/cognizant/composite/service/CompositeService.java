package com.cognizant.composite.service;

import com.cognizant.composite.dto.AccountDto;
import com.cognizant.composite.dto.CustomerPortfolioResponse;
import com.cognizant.composite.dto.LoanDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CompositeService {

    private final WebClient.Builder webClientBuilder;

    public CompositeService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    /**
     * Aggregates Account and Loan details in parallel using WebClient and Mono.zip().
     */
    public Mono<CustomerPortfolioResponse> getCustomerPortfolio(String accountNumber, String loanNumber) {
        Mono<AccountDto> accountMono = webClientBuilder.build()
                .get()
                .uri("http://account-service/accounts/{number}", accountNumber)
                .retrieve()
                .bodyToMono(AccountDto.class)
                .onErrorReturn(new AccountDto(accountNumber, "UNKNOWN", 0.0));

        Mono<LoanDto> loanMono = webClientBuilder.build()
                .get()
                .uri("http://loan-service/loans/{number}", loanNumber)
                .retrieve()
                .bodyToMono(LoanDto.class)
                .onErrorReturn(new LoanDto(loanNumber, "UNKNOWN", 0.0, 0.0, 0));

        return Mono.zip(accountMono, loanMono)
                .map(tuple -> new CustomerPortfolioResponse(
                        tuple.getT1(),
                        tuple.getT2(),
                        "SUCCESS"
                ));
    }
}
