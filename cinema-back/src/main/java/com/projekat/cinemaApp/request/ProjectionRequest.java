package com.projekat.cinemaApp.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class ProjectionRequest {
	
	private Long movieId;
	
	private String projectionType;
	
	private Long hallId;
	
	private LocalDateTime dateTime;
	
	public Double ticketPrice;
}
