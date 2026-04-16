package com.healthcare.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Report {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String patientName;
	private String fileName;
	private String fileType;
	
	@Lob
	private byte[] data;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date uploadedDate;

	@ManyToOne
	@JoinColumn(name= "appointment_id")
	private Appointment appointment;
	
	@PrePersist
	protected void onCreate() {
		uploadedDate = new Date();
	}
}
