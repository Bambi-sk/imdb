package com.javaproject.imbd.repositories;

import com.javaproject.imbd.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface GenreRepository extends JpaRepository<Genre,Long> {
}
