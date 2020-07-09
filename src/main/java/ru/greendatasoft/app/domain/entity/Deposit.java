package ru.greendatasoft.app.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Deposit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Bank bank;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Customer customer;

    @Column(columnDefinition = "DATE", nullable = false)
    private LocalDate openDate;
    @Column(nullable = false)
    private BigDecimal percent;
    @Column(nullable = false)
    private Integer months;
    @Version
    private Long version;
}