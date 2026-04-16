package com.healthcare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthcare.entities.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
	List<Report> findByPatientName(String pateintName);

	
}
