package com.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CenterResponse {

	private Long id;
	private String ownerName;
	private String centerName;
	private String location;
	private String status;
}
