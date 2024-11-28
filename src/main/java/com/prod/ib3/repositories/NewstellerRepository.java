package com.prod.ib3.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prod.ib3.entities.Newsteller;

@Repository
public interface NewstellerRepository extends JpaRepository<Newsteller, Long> {

    Optional<Newsteller> findByEmail(String email);
}
