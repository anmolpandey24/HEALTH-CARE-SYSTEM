package com.healthcare.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class DiagnosticTest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@JsonProperty("testName")
	private String testname;
	@JsonProperty("testPrice")
	private Double price;
	private String description;
	public DiagnosticTest(String testname, Double price) {
		super();
		this.testname = testname;
		this.price = price;
	}
	
	
}
