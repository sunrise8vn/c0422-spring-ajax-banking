package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.dto.CustomerDTO;
import com.cg.service.IGeneralService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ICustomerService extends IGeneralService<Customer> {

    List<Customer> findAllByIdNot(Long senderId);

    Customer deposit(Deposit deposit);

    public Map<String, CustomerDTO> transfer(Transfer transfer);

    void incrementMoney(BigDecimal transactionAmount, Long customerId);

    void reduceMoney(BigDecimal transactionAmount, Long customerId);
}
