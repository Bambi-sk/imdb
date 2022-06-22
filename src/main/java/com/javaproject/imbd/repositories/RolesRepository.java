package com.javaproject.imbd.repositories;

import com.javaproject.imbd.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface RolesRepository extends JpaRepository<Roles,Long> {
    Optional<Roles> findByRole(String name);
}
