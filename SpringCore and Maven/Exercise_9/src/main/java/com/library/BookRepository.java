package com.library;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA Repository interface for Book entities in Exercise 9.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
