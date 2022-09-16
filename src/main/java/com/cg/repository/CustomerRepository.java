package com.cg.repository;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Modifying
    @Query("" +
            "UPDATE Customer AS c " +
            "SET c.balance = c.balance + :transactionAmount " +
            "WHERE c.id = :customerId"
    )
    void incrementMoney(@Param("transactionAmount") BigDecimal transactionAmount, @Param("customerId") Long customerId);

    @Modifying
    @Query("" +
            "UPDATE Customer AS c " +
            "SET c.balance = c.balance - :transactionAmount " +
            "WHERE c.id = :customerId"
    )
    void reduceMoney(@Param("transactionAmount") BigDecimal transactionAmount, @Param("customerId") Long customerId);


    List<Customer> findAllByIdNot(Long senderId);
}
