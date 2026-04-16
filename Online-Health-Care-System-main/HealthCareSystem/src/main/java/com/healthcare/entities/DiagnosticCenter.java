package com.healthcare.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class DiagnosticCenter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String ownerName;
	private String centerName;
	@JsonProperty("address")
	private String location;
	private String status;
	
	@OneToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@JsonProperty("adminUsername")
	public String getAdminUsername() {
		return user != null ? user.getUsername() : null;
	}
	
//	@ManyToMany
//	@JoinTable(name="center_tests", joinColumns=@JoinColumn(name="center_id"), inverseJoinColumns = @JoinColumn(name="test_id"))
//	private List<DiagnosticTest> tests = new ArrayList<>();
}
