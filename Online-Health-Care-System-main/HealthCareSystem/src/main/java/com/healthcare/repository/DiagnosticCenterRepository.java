package com.healthcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.healthcare.entities.DiagnosticCenter;

public interface DiagnosticCenterRepository extends JpaRepository<DiagnosticCenter, Long>{
	
	
	List<DiagnosticCenter> findByStatus(String status);
	DiagnosticCenter findByUserId(Long userId);

}
