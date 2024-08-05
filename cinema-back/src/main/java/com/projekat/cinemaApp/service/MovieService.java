package com.projekat.cinemaApp.service;


import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;

import com.projekat.cinemaApp.exceptions.InternalServerException;
import com.projekat.cinemaApp.exceptions.ResourceNotFoundException;
import com.projekat.cinemaApp.model.Movie;
import com.projekat.cinemaApp.repository.MovieRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MovieService implements IMovieService {
	
	
	private final MovieRepository movieRepository;
	
	@Override
	public Movie addNewMovie(String name, Integer duration, String distributor, String country, Integer year,
			String description, Blob photo) throws IOException, SQLException {
		Movie movie = new Movie();
        movie.setName(name);
        movie.setDuration(duration);
        movie.setDistributor(distributor);
        movie.setCountry(country);
        movie.setYear(year);
        movie.setDescription(description);
        
        if (photo != null) {
            movie.setPhoto(photo);
        }
        
        return movieRepository.save(movie);
	}

	@Override
	public List<Movie> getAllMovies() {
		return movieRepository.findAll();
	}

	@Override
	public Movie updateMovie(Long movieId,String name, Integer duration, String distributor, String country, Integer year, String description, byte[] photoBytes) {
		Movie movie = movieRepository.findById(movieId).orElseThrow(
				() -> new IllegalArgumentException("Movie not found with id: " + movieId));
		movie.setName(name);
		movie.setDuration(duration);
		movie.setDistributor(distributor);
		movie.setCountry(country);
		movie.setYear(year);
		movie.setDescription(description);
		if(photoBytes != null && photoBytes.length > 0) {
			try {
				movie.setPhoto(new SerialBlob(photoBytes));
			}
			catch(SQLException e) {
				throw new InternalServerException("Fail updating movie.");
			}
		}
		return movieRepository.save(movie);
	}

	@Override
	public void delete(Long movieId) {
		Optional<Movie> theMovie = movieRepository.findById(movieId);
		if(theMovie.isPresent()) {
			movieRepository.deleteById(movieId);
		}
		
	}

	@Override
	public Optional<Movie> getMovieById(Long movieId) {
		return movieRepository.findById(movieId);
	}

	@Override
	public List<Movie> getMoviesByDuration(Integer durationFrom, Integer durationTo) {
		return movieRepository.findByDuration(durationFrom, durationTo);
	}

	@Override
	public byte[] getMoviePhotoByMovieId(Long movieId) throws SQLException {
		Optional<Movie> theMovie = movieRepository.findById(movieId);
		if(theMovie.isEmpty()) {
			throw new ResourceNotFoundException("Sorry, Movie not found");
		}
		Blob photoBlob = theMovie.get().getPhoto();
		if(photoBlob != null) {
			return photoBlob.getBytes(1, (int)photoBlob.length());
		}
		return null;
	}

}
