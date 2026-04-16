package com.healthcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthcare.entities.DiagnosticTest;

public interface DiagnosticTestRepository extends JpaRepository<DiagnosticTest, Long>{

	
}
