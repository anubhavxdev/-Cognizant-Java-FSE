package com.cognizant.composite.controller;

import com.cognizant.composite.dto.CustomerPortfolioResponse;
import com.cognizant.composite.service.CompositeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/composite")
public class CompositeController {

    private final CompositeService compositeService;

    public CompositeController(CompositeService compositeService) {
        this.compositeService = compositeService;
    }

    @GetMapping("/portfolio/{accountNumber}/{loanNumber}")
    public Mono<CustomerPortfolioResponse> getPortfolio(
            @PathVariable String accountNumber,
            @PathVariable String loanNumber) {
        return compositeService.getCustomerPortfolio(accountNumber, loanNumber);
    }
}
