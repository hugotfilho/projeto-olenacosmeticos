package com.prod.ib3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prod.ib3.entities.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
}
