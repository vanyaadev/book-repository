package com.vanyaadev.bookrepository.repository;

import com.vanyaadev.bookrepository.model.Firm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FirmRepository extends JpaRepository<Firm, Long> {

    Optional<Firm> findByNameAndCity(String name, String city);
}
