package com.javaproject.imbd.repositories;

import com.javaproject.imbd.entities.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface ReviewsRepository extends JpaRepository<Reviews,Long> {
    List<Reviews> findAllByMovieId(Long id);
}
