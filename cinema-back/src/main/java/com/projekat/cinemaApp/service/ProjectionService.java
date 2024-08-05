package com.projekat.cinemaApp.service;

import java.time.LocalDateTime;
import java.util.List;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.projekat.cinemaApp.model.Hall;
import com.projekat.cinemaApp.model.Movie;
import com.projekat.cinemaApp.model.Projection;
import com.projekat.cinemaApp.model.ProjectionType;
import com.projekat.cinemaApp.repository.HallRepository;
import com.projekat.cinemaApp.repository.MovieRepository;
import com.projekat.cinemaApp.repository.ProjectionRepository;
import com.projekat.cinemaApp.repository.ProjectionTypeRepository;

import lombok.RequiredArgsConstructor;



@RequiredArgsConstructor
@Service
public class ProjectionService implements IProjectionService {

	private static final Logger logger = LoggerFactory.getLogger(ProjectionService.class);

    private final ProjectionRepository projectionRepository;
    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;
    private final ProjectionTypeRepository typeRepository;

    @Override
    public Projection addNewProjection(Long movieId, String projectionType, Long hallId, LocalDateTime dateTime,
                                       Double ticketPrice) {
        logger.info("Received request to add new projection with movieId: {}, projectionType: {}, hallId: {}, dateTime: {}, ticketPrice: {}",
                movieId, projectionType, hallId, dateTime, ticketPrice);

        if (movieId == null) {
            throw new IllegalArgumentException("Movie ID must not be null");
        }
        if (hallId == null) {
            throw new IllegalArgumentException("Hall ID must not be null");
        }
        if (projectionType == null) {
            throw new IllegalArgumentException("Projection type must not be null");
        }

        Movie movie = movieRepository.findById(movieId).orElseThrow(
                () -> new IllegalArgumentException("Movie not found with id: " + movieId));
        Hall hall = hallRepository.findById(hallId).orElseThrow(
                () -> new IllegalArgumentException("Hall not found with id: " + hallId));
        ProjectionType type = typeRepository.findProjectionTypeByName(projectionType);
        if (type == null) {
            throw new IllegalArgumentException("Projection type not found: " + projectionType);
        }

        Projection projection = new Projection();
        projection.setMovie(movie);
        projection.setProjectionType(type);
        projection.setHall(hall);
        projection.setDateTime(dateTime);
        projection.setTicketPrice(ticketPrice);
        return projectionRepository.save(projection);
    }
	
	@Override
	public Projection updateProjecton(Long projectionId, Long movieId, String projectionType, Long hallId,
			LocalDateTime dateTime, Double ticketPrice) {
		Projection projection = projectionRepository.findById(projectionId).orElseThrow(
				() -> new IllegalArgumentException("Projection not found with id: " + projectionId));
		Movie movie = movieRepository.findById(movieId).orElseThrow(
				() -> new IllegalArgumentException("Movie not found with id: "+ movieId));
		Hall hall = hallRepository.findById(hallId).orElseThrow(
				() -> new IllegalArgumentException("Hall not found with id: " + hallId));
		ProjectionType type = typeRepository.findProjectionTypeByName(projectionType);
		if(movieId != null) {
			projection.setMovie(movie);
		}
		if(projectionType != null) {
			projection.setProjectionType(type);
		}
		if(hallId != null) {
			projection.setHall(hall);
		}
		if(dateTime != null) {
			projection.setDateTime(dateTime);
		}
		if(ticketPrice != null) {
			projection.setTicketPrice(ticketPrice);
		}
		return projectionRepository.save(projection);
	}

	@Override
	public Optional<Projection> getProjectionById(Long projectionId) {
		return projectionRepository.findById(projectionId);
	}

	@Override
	public List<Projection> getAllProjections() {
		return projectionRepository.findAll();
	}

	@Override
	public void delete(Long projectionId) {
		Projection projection = projectionRepository.findById(projectionId).orElseThrow(
				() -> new IllegalArgumentException("Projection with the given ID does not exist"));
		projectionRepository.delete(projection);
	}

	@Override
	public List<Projection> findByMovieId(Long movieId) {
		return projectionRepository.findByMovieId(movieId);
	}

	@Override
	public List<Projection> search(LocalDateTime dateFrom, LocalDateTime dateTo, Double priceFrom, Double priceTo,
			String projectionTypeName, Long movieId, String hallName) {
		return projectionRepository.search(dateFrom, dateTo, priceFrom, priceTo, projectionTypeName, movieId, hallName);
	}

	@Override
	public List<Projection> getProjectionsByDate(LocalDateTime dateFrom, LocalDateTime dateTo) {
		return projectionRepository.findProjectionsByDate(dateFrom, dateTo);
	}

	@Override
	public List<String> getProjectionsByProjectionType() {
		System.out.println(projectionRepository.findDistinctProjectionTypes());
		return projectionRepository.findDistinctProjectionTypes();
	}

	@Override
	public List<Projection> getAvailableProjections(LocalDateTime startDate, LocalDateTime endDate) {
		return projectionRepository.findAvailableProjectionsByDateRange(startDate, endDate);
	}

	
}
