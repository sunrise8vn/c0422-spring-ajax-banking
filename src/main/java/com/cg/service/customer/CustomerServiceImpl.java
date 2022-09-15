package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.repository.CustomerRepository;
import com.cg.repository.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DepositRepository depositRepository;

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getById(Long id) {
        return null;
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Customer deposit(Deposit deposit) {

        Long customerId = deposit.getCustomer().getId();

        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        Customer customer = customerOptional.get();

        customerRepository.incrementMoney(deposit.getTransactionAmount(), customerId);

        depositRepository.save(deposit);

        BigDecimal newBalance = customer.getBalance().add(deposit.getTransactionAmount());

        customer.setBalance(newBalance);

        return customer;
    }

    @Override
    public void incrementMoney(BigDecimal transactionAmount, Long customerId) {
        customerRepository.incrementMoney(transactionAmount, customerId);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void remove(Long id) {

    }
}
