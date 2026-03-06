package com.garagegenie.repository;

import com.garagegenie.entity.JobCard;
import com.garagegenie.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobCardRepository extends JpaRepository<JobCard, Long> {
    List<JobCard> findByCustomerId(Long customerId);

    List<JobCard> findByAssignedMechanicId(Long mechanicId);

    List<JobCard> findByStatus(Appointment.ServiceStatus status);
}
