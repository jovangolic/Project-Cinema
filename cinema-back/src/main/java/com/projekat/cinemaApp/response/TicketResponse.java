package com.projekat.cinemaApp.response;

import java.time.LocalDateTime;


import com.projekat.cinemaApp.dto.UserDTO;
import com.projekat.cinemaApp.enumeration.TicketStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Getter
//@Setter
public class TicketResponse {

	
	private Long id;
	
	private ProjectionTicketResponse projectionTicketResponse;
	
	private SeatResponse seatResponse;
	
	private LocalDateTime saleDateTime;
	
	private TicketStatus status;
	
	private UserDTO userDto;
	
	//private String ticketConfirmationCode;
	
	public TicketResponse(Long id, LocalDateTime saleDateTime, TicketStatus status) {
		this.id = id;
		this.saleDateTime = saleDateTime;
		this.status = status;
	}
	
	public TicketResponse(Long id, ProjectionTicketResponse projectionTicketResponse, SeatResponse seatResponse, LocalDateTime saleDateTime, TicketStatus status) {
		this.id = id;
		this.projectionTicketResponse = projectionTicketResponse;
		this.seatResponse = seatResponse;
		this.saleDateTime = saleDateTime;
		this.status = status;
		
	}
	
	
}
