package com.healthcare.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthcare.entities.CenterTest;

public interface CenterTestRepository extends JpaRepository<CenterTest, Long> {

	List<CenterTest> findByCenterId(Long centerId);
	List<CenterTest> findByCenterIdAndTestId(Long centerId, Long testId);
	 // boole existsByCenterIdAndTestId(Long centerId, Long testId);
}
