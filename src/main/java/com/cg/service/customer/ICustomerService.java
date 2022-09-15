package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.service.IGeneralService;

import java.math.BigDecimal;

public interface ICustomerService extends IGeneralService<Customer> {

    Customer deposit(Deposit deposit);

    void incrementMoney(BigDecimal transactionAmount, Long customerId);
}