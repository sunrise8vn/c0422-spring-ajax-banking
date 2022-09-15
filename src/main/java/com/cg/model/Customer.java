package com.cg.model;
import com.cg.model.dto.CustomerDTO;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 25, message = "Tên quá dài")
    @Column(name = "full_name")
    private String fullName;

    @NotEmpty(message = "The email address is required.")
    @Email(message = "The email address is invalid.")
    @Size(min = 5, max = 50, message = "The length of email must be between 5 and 50 characters.")
    @Column(unique = true, nullable = false)
    private String email;

    private String phone;
    private String address;

    @Column(precision = 12, updatable = false)
    private BigDecimal balance;


    @OneToMany(targetEntity = Deposit.class, mappedBy = "customer", fetch = FetchType.EAGER)
    private Set<Deposit> deposits;

    @OneToMany(targetEntity = Withdraw.class, mappedBy = "customer", fetch = FetchType.EAGER)
    private Set<Withdraw> withdraws;

    @OneToMany(targetEntity = Transfer.class, mappedBy = "sender", fetch = FetchType.EAGER)
    private Set<Transfer> sender;

    @OneToMany(targetEntity = Transfer.class, mappedBy = "recipient", fetch = FetchType.EAGER)
    private Set<Transfer> recipient;


    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", balance=" + balance +
                '}';
    }

    public CustomerDTO toCustomerDTO() {
        return new CustomerDTO()
                .setId(id)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setAddress(address)
                .setBalance(balance);
    }

}
