package com.cognizant.ormlearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cognizant.ormlearn.model.Country;
import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, String> {
    // Find list of countries matching a partial country name
    List<Country> findByNameContaining(String name);

    // Find list of countries matching a partial country name, sorted ascending
    List<Country> findByNameContainingOrderByNameAsc(String name);

    // Find list of countries starting with a specific letter
    List<Country> findByNameStartingWith(String letter);
}
