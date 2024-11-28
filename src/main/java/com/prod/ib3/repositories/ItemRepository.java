package com.prod.ib3.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prod.ib3.entities.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}
