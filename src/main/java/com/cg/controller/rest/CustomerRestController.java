package com.cg.controller.rest;


import com.cg.model.Customer;
import com.cg.model.dto.CustomerDTO;
import com.cg.service.customer.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {

    @Autowired
    private ICustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getList() {

        List<CustomerDTO> customerDTOS = new ArrayList<>();

        List<Customer> customers = customerService.findAll();

        for (Customer customer : customers) {
            CustomerDTO customerDTO = customer.toCustomerDTO();

            customerDTOS.add(customerDTO);
        }

        return new ResponseEntity<>(customerDTOS, HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> getById(@PathVariable Long customerId) {
        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Customer customer = customerOptional.get();

        CustomerDTO customerDTO = customer.toCustomerDTO();

        return new ResponseEntity<>(customerDTO, HttpStatus.OK);
    }


    @PostMapping("/create")
    public ResponseEntity<Customer> doCreate(@RequestBody Customer customer) {

        Customer newCustomer = customerService.save(customer);


        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }
}
