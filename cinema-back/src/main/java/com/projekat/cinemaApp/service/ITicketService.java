package com.projekat.cinemaApp.service;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.projekat.cinemaApp.enumeration.TicketStatus;
import com.projekat.cinemaApp.model.Projection;
import com.projekat.cinemaApp.model.Seat;
import com.projekat.cinemaApp.model.Ticket;
import com.projekat.cinemaApp.request.TicketRequest;

public interface ITicketService {

	Ticket addNewTicket(Long projectionId,Integer seatNumber,Double price,Long userId,String email,Integer quantity, TicketStatus status) throws SQLException, IOException;
	
	Optional<Ticket> getTicketById(Long tickeId);
	
	List<Ticket> getAllTickets();
	
	void deleteTicket(Long ticketId);
	
	List<Ticket> getAvailableTickets(LocalDateTime dates, Projection projection, TicketStatus status);
	
	Ticket updateTicket(Long ticketId,Long projectionId,Integer seatNumber,Double price,Long userId,String email,Integer quantity);
	
	//Ticket findByTicketConfirmationCode(String confirmationCode);
	
	String saveTicket(Long projectionId, TicketRequest ticketRequest, TicketStatus status, String confirmationCode);
	
	List<Ticket> getTicketsByUserEmail(String email);
	
	Ticket updateTicketStatus(Long ticketId, TicketStatus status);
    
    Ticket purchaseReservedTicket(Long ticketId);
    
    Ticket cancelReservedTicket(Long ticketId);
    
    Ticket cancelPurchasedTicket(Long ticketId);
    
    List<Ticket> getTicketsByStatus(TicketStatus status);
    
    List<Seat> getAvailableSeats(Long projectionId);
    
    Double countTicketsByProjection(Long projectionId);
    
    List<Ticket> getTicketsByComfirmCode(String confirmCode);
}
