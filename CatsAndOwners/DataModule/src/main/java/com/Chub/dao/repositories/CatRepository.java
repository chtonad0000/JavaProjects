package com.Chub.dao.repositories;

import com.Chub.dao.Models.cats.Cat;
import com.Chub.dao.Models.cats.CatBreed;
import com.Chub.dao.Models.cats.CatColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CatRepository extends JpaRepository<Cat, Long> {
    @Query("SELECT cat FROM Cat cat WHERE cat.color = :color")
    Set<Cat> findCatsByColor(@Param("color") CatColor color);

    @Query("SELECT cat FROM Cat cat WHERE cat.breed = :breed")
    Set<Cat> findCatsByBreed(@Param("breed")CatBreed breed);
}
