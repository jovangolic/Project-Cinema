package com.projekat.cinemaApp.service;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.projekat.cinemaApp.enumeration.TicketStatus;
import com.projekat.cinemaApp.exceptions.ResourceNotFoundException;
import com.projekat.cinemaApp.model.Projection;
import com.projekat.cinemaApp.model.Seat;
import com.projekat.cinemaApp.model.Ticket;
import com.projekat.cinemaApp.repository.ProjectionRepository;
import com.projekat.cinemaApp.repository.SeatRepository;
import com.projekat.cinemaApp.repository.TicketRepository;
import com.projekat.cinemaApp.repository.UserRepository;
import com.projekat.cinemaApp.request.TicketRequest;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class TicketService implements ITicketService {
	
	private final TicketRepository ticketRepository;
	private final SeatRepository seatRepository;
	private final ProjectionRepository projectionRepository;
	private final UserRepository userRepository;
	
	@Override
	public Ticket addNewTicket(Long projectionId,Integer seatNumber,Double price,Long userId,String email,Integer quantity, TicketStatus status) throws SQLException, IOException {
		Projection projection = projectionRepository.findById(projectionId).orElseThrow(
				() -> new IllegalArgumentException("Projection not found"));
		// Pronalazi odgovarajuce sediste na osnovu broja sedista i sale projekcije
		Seat seat = seatRepository.findBySeatNumberAndHall(seatNumber, projection.getHall()).orElseThrow(
				() -> new IllegalArgumentException("Seat not found for a given hall!"));
		// Provera da li je sediste već zauzeto za tu projekciju
		boolean isSeatTaken = projection.getTickets().stream().anyMatch(ticket -> ticket.getSeat().equals(seat));
		if(isSeatTaken) {
			throw new IllegalArgumentException("Seat is already taken for this projection!");
		}
		//kreiranje nove karte
		Ticket ticket = new Ticket();
		ticket.setProjection(projection);
		ticket.setSeat(seat);
		ticket.setSaleDateTime(LocalDateTime.now());
		ticket.setPrice(price);
		ticket.setStatus(status);
		//dodavanje karte u projekciju
		projection.getTickets().add(ticket);
		//cuvanje u bazi
		return ticketRepository.save(ticket);
	}
	@Override
	public Optional<Ticket> getTicketById(Long tickeId) {
		return ticketRepository.findById(tickeId);
	}
	@Override
	public List<Ticket> getAllTickets() {
		return ticketRepository.findAll();
	}
	
	@Override
    public void deleteTicket(Long ticketId) {
        if (ticketRepository.existsById(ticketId)) {
            ticketRepository.deleteById(ticketId);
        } else {
            throw new IllegalArgumentException("Ticket not found with id: " + ticketId);
        }
    }
	
	@Override
	public List<Ticket> getAvailableTickets(LocalDateTime dates, Projection projection, TicketStatus status) {
		return ticketRepository.findAvailableTicketsBySaleDateTimeAndProjectionAndStatus(dates, projection, status);
	}
	@Override
	public Ticket updateTicket(Long ticketId,Long projectionId,Integer seatNumber,Double price,Long userId,String email,Integer quantity) {
		Projection projection = projectionRepository.findById(projectionId).orElseThrow(
				() -> new IllegalArgumentException("Projection not found"));
		Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(
				() -> new IllegalArgumentException("Ticket not found with id: " + ticketId));
		Seat seat = seatRepository.findBySeatNumberAndHall(seatNumber, projection.getHall()).orElseThrow(
				() -> new IllegalArgumentException("Seat not found for a given hall!"));
		boolean isSeatTaken = projection.getTickets().stream().anyMatch(
				t -> t.getSeat().equals(seat));
		if(isSeatTaken) {
			throw new IllegalArgumentException("Seat is already taken for this projection!");
		}
		ticket.setProjection(projection);
		ticket.setSeat(seat);
		ticket.setSaleDateTime(LocalDateTime.now());
		ticket.setPrice(price);
		return ticketRepository.save(ticket);
	}
	/*@Override
	public Ticket findByTicketConfirmationCode(String confirmationCode) {
		return ticketRepository.findByTicketConfirmationCode(confirmationCode).orElseThrow(
				() -> new ResourceNotFoundException("No ticket found with ticket code: " + confirmationCode));
	}*/
	
	
	@Override
	public List<Ticket> getTicketsByUserEmail(String email) {
		return ticketRepository.findByUserEmail(email);
	}
	
	@Override
	public String saveTicket(Long projectionId, TicketRequest ticketRequest, TicketStatus status, String confirmationCode) {
	    Projection projection = projectionRepository.findById(projectionId)
	            .orElseThrow(() -> new ResourceNotFoundException("Projection not found with id: " + projectionId));
	    Seat seat = seatRepository.findBySeatNumberAndHall(ticketRequest.getSeatNumber(), projection.getHall())
	            .orElseThrow(() -> new IllegalArgumentException("Seat not found for the given hall!"));

	    boolean isSeatTaken = projection.getTickets().stream()
	            .anyMatch(ticket -> ticket.getSeat().equals(seat));
	    if (isSeatTaken) {
	        throw new IllegalArgumentException("Seat is already taken for this projection!");
	    }
	    
	    Ticket ticket = new Ticket();
	    ticket.setProjection(projection);
	    ticket.setSeat(seat);
	    ticket.setPrice(ticketRequest.getPrice());
	    ticket.setStatus(status);
	    ticket.setSaleDateTime(LocalDateTime.now());
	    ticket.setTicketConfirmationCode(confirmationCode);
	    ticket.setUser(userRepository.findByEmail(ticketRequest.getEmail()).orElseThrow(
	            () -> new IllegalArgumentException("User not found!")));
	    projection.getTickets().add(ticket);
	    projectionRepository.save(projection);
	    ticketRepository.save(ticket);
	    return ticket.getTicketConfirmationCode();
	}
	
	
	private boolean projectionIsAvailable(Projection projectionRequest, List<Projection> existingProjections) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime requestSaleDate = projectionRequest.getDateTime();
        if (requestSaleDate.isBefore(now)) {
            return false;
        }
        // Proverava da li postoji karta sa istim ili preklapajućim datumom i vremenom prodaje
        return existingProjections.stream().noneMatch(
                existingProjection -> requestSaleDate.isEqual(existingProjection.getDateTime()) ||
                        requestSaleDate.isBefore(existingProjection.getDateTime()));
    }
	@Override
	public Ticket updateTicketStatus(Long ticketId, TicketStatus status) {
		Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(
				() -> new IllegalArgumentException("Ticket not found with id: " + ticketId));
		//menjanje statusa
		if(ticket.getProjection().getDateTime().isBefore(LocalDateTime.now())) {
			ticket.setStatus(status);
			return ticketRepository.save(ticket);
		}
		else {
			throw new IllegalArgumentException("Date must not be in the past");
		}
	}
	
	@Override
	public Ticket purchaseReservedTicket(Long ticketId) {
		return updateTicketStatus(ticketId, TicketStatus.PURCHASED);
	}
	
	@Override
	public Ticket cancelReservedTicket(Long ticketId) {
		return updateTicketStatus(ticketId, TicketStatus.CANCELLED);
	}
	
	@Override
	public Ticket cancelPurchasedTicket(Long ticketId) {
		return updateTicketStatus(ticketId, TicketStatus.CANCELLED);
	}
	@Override
	public List<Ticket> getTicketsByStatus(TicketStatus status) {
		return ticketRepository.findByStatus(status);
	}
	/*@Override
	public List<Seat> getAvailableSeats(Long projectionId) {
		Projection projection = projectionRepository.findById(projectionId).orElseThrow(
				() -> new ResourceNotFoundException("Projection not found"));
		List<Ticket> tickets = ticketRepository.findByProjectionAndStatus(projection, TicketStatus.AVAILABLE);
		return tickets.stream().map(Ticket::getSeat).collect(Collectors.toList());
	}*/
	@Override
	public List<Seat> getAvailableSeats(Long projectionId){
		Projection projection = projectionRepository.findById(projectionId).orElseThrow(
				() -> new ResourceNotFoundException("Projection not found"));
		List<Seat> allSeats = seatRepository.findByHall(projection.getHall());
		List<Ticket> occupiedTickets = ticketRepository.findByProjectionAndStatus(projection, TicketStatus.AVAILABLE);
		Set<Seat> occupiedSeats = occupiedTickets.stream().map(Ticket::getSeat).collect(Collectors.toSet());
		return allSeats.stream().filter(
				seat -> !occupiedSeats.contains(seat)).collect(Collectors.toList());
	}
	@Override
	public Double countTicketsByProjection(Long projectionId) {
		return ticketRepository.countByProjectionId(projectionId);
	}
	@Override
	public List<Ticket> getTicketsByComfirmCode(String confirmCode) {
		return ticketRepository.findByTicketConfirmationCode(confirmCode);
	}
}
