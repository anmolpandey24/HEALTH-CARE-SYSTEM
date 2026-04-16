package com.healthcare.mapper;

import com.healthcare.dto.CenterRequest;
import com.healthcare.dto.CenterResponse;
import com.healthcare.entities.DiagnosticCenter;

public class CenterMapper {

	public static DiagnosticCenter toEntity(CenterRequest req) {
		DiagnosticCenter c =  new DiagnosticCenter();
		c.setOwnerName(req.getOwnerName());
		c.setCenterName(req.getCenterName());
		c.setLocation(req.getAddress());
		return c;
	}
	
	public static CenterResponse toResponse(DiagnosticCenter c) {
		CenterResponse res = new CenterResponse();
		res.setId(c.getId());
		res.setOwnerName(c.getOwnerName());
		res.setCenterName(c.getCenterName());
		res.setLocation(c.getLocation());
		res.setStatus(c.getStatus());
		return res;
	}
}
