package com.garagegenie.repository;

import com.garagegenie.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByCustomerId(Long customerId);

    Optional<Invoice> findByJobCardId(Long jobCardId);

    List<Invoice> findByStatus(Invoice.PaymentStatus status);
}
