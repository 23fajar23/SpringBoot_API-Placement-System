package com.placement.Placement.repository;

import com.placement.Placement.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message,String> {

    @Query("SELECT ms FROM Message ms WHERE ms.recipient = :customer_id")
    Optional<Message> findByCustomer(@Param("customer_id") String customer_id);
}
