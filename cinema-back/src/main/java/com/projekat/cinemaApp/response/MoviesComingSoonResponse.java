package com.projekat.cinemaApp.response;

import java.time.LocalDate;
import java.util.Base64;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
//@AllArgsConstructor
@Data
public class MoviesComingSoonResponse {

	
	private Long id;
	
	private String name;
	
    private Integer duration;
    
    private String distributor;
    
    private String country;
    
    private Integer year;
    
    private String description;
    
    private LocalDate releaseDate;
    
    private String photo;
    
    public MoviesComingSoonResponse(Long id, String name, Integer duration) {
		this.id = id;
		this.name = name;
		this.duration = duration;
	}
	
	public MoviesComingSoonResponse(Long id, String name, Integer duration, byte[] photoBytes) {
		this.id = id;
		this.name = name;
		this.duration = duration;
		this.photo = photoBytes != null ? Base64.getEncoder().encodeToString(photoBytes) : null;
	}
	
	public MoviesComingSoonResponse(Long id, String name, Integer duration, String distributor,
			String country, Integer year, String description, LocalDate releaseDate, byte[] photoBytes
			) {
		this.id = id;
		this.name = name;
		this.duration = duration;
		this.distributor = distributor;
		this.country = country;
		this.year = year;
		this.description = description;
		this.releaseDate = releaseDate;
		this.photo = photoBytes != null ? Base64.getEncoder().encodeToString(photoBytes) : null;
	}
}
