package com.projekat.cinemaApp.request;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class TicketRequest {

	private Long projectionId;
	private Integer seatNumber;
    private Double price;
    private Long userId;
    private String email;
    private Integer quantity;
}
