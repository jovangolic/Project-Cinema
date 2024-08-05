package com.projekat.cinemaApp.request;

import java.util.Base64;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MovieRequest {

	private String name;
	
	private Integer duration;
	
	private String distributor;
	
	private String country;
	
	private Integer year;
	
	private String description;
	
	private String photo;
	
	public MovieRequest(String name, Integer duration, String distributor,String country,
			Integer year, String description, byte[] photoBytes) {
		this.name = name;
		this.duration = duration;
		this.distributor = distributor;
		this.country = country;
		this.year = year;
		this.description = description;
		this.photo = photoBytes != null ? Base64.getEncoder().encodeToString(photoBytes) : null;
	}
}
