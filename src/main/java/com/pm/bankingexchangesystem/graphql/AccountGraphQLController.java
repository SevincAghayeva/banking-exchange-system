package com.pm.bankingexchangesystem.graphql;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Controller
public class AccountGraphQLController {


    private final BankAccountRepository repository;

    @QueryMapping
    public BankAccount getAccountById(@Argument Long id) {
        log.info("[GraphQL Query] PostgreSQL-dən hesab axtarılır. ID: {}", id);
        return repository.findById(id).orElse(null);
    }

    @QueryMapping
    public List<BankAccount> getAllAccounts() {
        log.info("[GraphQL Query] PostgreSQL-dəki bütün hesablar gətirilir.");
        return repository.findAll();
    }

    @MutationMapping
    public BankAccount createNewAccount(@Argument String holderName,
                                        @Argument Double balance,
                                        @Argument String currency) {
        log.info("[GraphQL Mutation] PostgreSQL-də yeni hesab yaradılır. Sahibi: {}", holderName);

        BankAccount account = new BankAccount(null,holderName, balance, currency);

        return repository.save(account);
    }
}