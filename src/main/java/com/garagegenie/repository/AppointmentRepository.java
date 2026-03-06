package com.garagegenie.repository;

import com.garagegenie.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByCustomerId(Long customerId);

    List<Appointment> findByVehicleId(Long vehicleId);

    List<Appointment> findByStatus(Appointment.ServiceStatus status);
}
