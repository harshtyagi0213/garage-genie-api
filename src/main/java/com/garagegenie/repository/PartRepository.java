package com.garagegenie.repository;

import com.garagegenie.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {
    List<Part> findByCategory(String category);

    @Query("SELECT p FROM Part p WHERE p.quantity <= p.lowStockThreshold")
    List<Part> findLowStockParts();
}
