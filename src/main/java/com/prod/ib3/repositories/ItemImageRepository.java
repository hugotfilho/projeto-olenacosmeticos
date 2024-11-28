package com.prod.ib3.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.prod.ib3.entities.ItemImage;

import jakarta.transaction.Transactional;

@Repository
public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {
    List<ItemImage> findItemImagesById(Long id);

    List<ItemImage> findByItemId(Long itemId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ItemImage ii WHERE ii.item.id = :itemId")
    void deleteByItemId(Long itemId);
}
