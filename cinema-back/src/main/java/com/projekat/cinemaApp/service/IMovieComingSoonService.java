package com.projekat.cinemaApp.service;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.projekat.cinemaApp.model.MoviesComingSoon;

public interface IMovieComingSoonService {
	
	
	MoviesComingSoon addNewMovie(String name, Integer duration, String distributor, 
			String country, Integer year, String description, LocalDate releaseDate, Blob photo) throws IOException, SQLException;
	
	void deleteMovie(Long movieId);
	
	List<MoviesComingSoon> findUpcomingMovies();
	
	List<MoviesComingSoon> allUpcomingMovies();
	
	byte[] getMoviePhotoByMovieId(Long movieId) throws SQLException;
	
	Optional<MoviesComingSoon> getMovieDetails(Long movieComingSoonId) throws SQLException;
}
