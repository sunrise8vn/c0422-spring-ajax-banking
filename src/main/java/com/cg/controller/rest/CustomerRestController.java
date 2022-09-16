package com.cg.controller.rest;


import com.cg.model.Customer;
import com.cg.model.Transfer;
import com.cg.model.dto.CustomerDTO;
import com.cg.model.dto.TransferCreateDTO;
import com.cg.service.customer.ICustomerService;
import com.cg.service.transfer.ITransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;


@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ITransferService transferService;

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

    @GetMapping("/get-all-recipients-without-sender/{senderId}")
    public ResponseEntity<?> getAllRecipientsWithoutSender(@PathVariable Long senderId) {

        Optional<Customer> senderOptional = customerService.findById(senderId);

        if (!senderOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Customer> recipients = customerService.findAllByIdNot(senderId);

        List<CustomerDTO> recipientDTOs = new ArrayList<>();

        for (Customer item : recipients) {
            CustomerDTO customerDTO = item.toCustomerDTO();

            recipientDTOs.add(customerDTO);
        }

        return new ResponseEntity<>(recipientDTOs, HttpStatus.OK);
    }


    @PostMapping("/create")
    public ResponseEntity<Customer> doCreate(@RequestBody Customer customer) {

        Customer newCustomer = customerService.save(customer);


        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> doTransfer(@RequestBody TransferCreateDTO transferCreateDTO) {

        Long senderId = transferCreateDTO.getSenderId();
        Long recipientId = transferCreateDTO.getRecipientId();

        if (senderId.equals(recipientId)) {
            return new ResponseEntity<>("Người gửi và người nhận không hợp lệ", HttpStatus.BAD_REQUEST);
        }

        Optional<Customer> senderOptional = customerService.findById(senderId);

        if (!senderOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Customer> recipientOptional = customerService.findById(recipientId);

        if (!recipientOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        BigDecimal senderBalance = senderOptional.get().getBalance();

        BigDecimal transferAmount = transferCreateDTO.getTransferAmount();
        long fees = 10L;
        BigDecimal feesAmount = transferAmount.multiply(new BigDecimal(fees)).divide(new BigDecimal(100L));
        BigDecimal transactionAmount = transferAmount.add(feesAmount);

        if (senderBalance.compareTo(transactionAmount) < 0) {
            return new ResponseEntity<>("Số dư không đủ để thực hiện giao dịch", HttpStatus.BAD_REQUEST);
        }

        transferCreateDTO.setFees(fees);
        transferCreateDTO.setFeesAmount(feesAmount);
        transferCreateDTO.setTransactionAmount(transactionAmount);
        Transfer transfer = transferCreateDTO.toTransfer(senderOptional.get(), recipientOptional.get());

        Map<String, CustomerDTO> results = new HashMap<>();

        results = customerService.transfer(transfer);

        return new ResponseEntity<>(results, HttpStatus.CREATED);
    }
}
