package com.javaproject.imbd.repositories;

import com.javaproject.imbd.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
@Repository
public interface PersonRepository extends JpaRepository<Person,Long> {
    List<Person> findAllByJobsId(Long id);
}
