package com.projekat.cinemaApp.controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.projekat.cinemaApp.exceptions.PhotoRetrievalException;
import com.projekat.cinemaApp.model.MoviesComingSoon;
import com.projekat.cinemaApp.response.MoviesComingSoonResponse;
import com.projekat.cinemaApp.service.IMovieComingSoonService;

import lombok.RequiredArgsConstructor;

@CrossOrigin("http://localhost:5173")
@RestController
@RequestMapping("/movieComingSoon")
@RequiredArgsConstructor
public class MoviesComingSoonController {
	
	
	private final IMovieComingSoonService comingSoonService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/add/new-movie-coming-soon")
	public ResponseEntity<MoviesComingSoonResponse> addNewMovie(
			@RequestParam("name") String name,
	        @RequestParam("duration") Integer duration,
	        @RequestParam("distributor") String distributor,
	        @RequestParam("country") String country,
	        @RequestParam("year") Integer year,
	        @RequestParam("description") String description,
	        @RequestParam("releaseDate") LocalDate releaseDate,
	        @RequestParam("photo") MultipartFile photo) throws SQLException, IOException {
		byte[] photoBytes = photo != null && !photo.isEmpty() ? photo.getBytes() : null;
	    Blob photoBlob = photoBytes != null ? new SerialBlob(photoBytes) : null;
	    MoviesComingSoon theMovie = comingSoonService.addNewMovie(name, duration, distributor, country, year, description, releaseDate, photoBlob);
	    MoviesComingSoonResponse response = new MoviesComingSoonResponse(theMovie.getId(),theMovie.getName(),theMovie.getDuration(),
	    		theMovie.getDistributor(),theMovie.getCountry(),theMovie.getYear(),
	    		theMovie.getDescription(),theMovie.getReleaseDate(), photoBytes);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/delete/comingSoon/{movieComingSoonId}")
	public ResponseEntity<Void> delete(@PathVariable Long movieComingSoonId){
		comingSoonService.deleteMovie(movieComingSoonId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
	@GetMapping("/all-upcoming-movies")
	public ResponseEntity<List<MoviesComingSoonResponse>> getAllUpcomingMovies() throws SQLException{
		List<MoviesComingSoon> lists = comingSoonService.allUpcomingMovies();
		List<MoviesComingSoonResponse> responses = new ArrayList<>();
		for(MoviesComingSoon movie : lists) {
			byte[] photoBytes = comingSoonService.getMoviePhotoByMovieId(movie.getId());
			if(photoBytes != null && photoBytes.length > 0) {
				String base64Photo = Base64.getEncoder().encodeToString(photoBytes);
				MoviesComingSoonResponse response = getResponse(movie);
				response.setPhoto(base64Photo);
				responses.add(response);
			}
		}
		return ResponseEntity.ok(responses);
	}
	
	@GetMapping("/movie/{movieComingSoonId}")
    public ResponseEntity<MoviesComingSoonResponse> getMovieDetails(@PathVariable Long movieComingSoonId) throws SQLException {
        MoviesComingSoon movie = comingSoonService.getMovieDetails(movieComingSoonId).orElseThrow(
                () -> new IllegalArgumentException("Movie not found"));
        byte[] photoBytes = comingSoonService.getMoviePhotoByMovieId(movie.getId());
        String base64Photo = photoBytes != null ? Base64.getEncoder().encodeToString(photoBytes) : null;
        MoviesComingSoonResponse response = getResponse(movie);
        response.setPhoto(base64Photo);
        return ResponseEntity.ok(response);
    }
	
	@GetMapping("/find-upcoming-movies")
	public ResponseEntity<List<MoviesComingSoonResponse>> findUpcomingMovies() throws SQLException{
		List<MoviesComingSoon> lists = comingSoonService.findUpcomingMovies();
		List<MoviesComingSoonResponse> responses = new ArrayList<>();
		for(MoviesComingSoon movie : lists) {
			byte[] photoBytes = comingSoonService.getMoviePhotoByMovieId(movie.getId());
			if(photoBytes != null && photoBytes.length > 0) {
				String base64Photo = Base64.getEncoder().encodeToString(photoBytes);
				MoviesComingSoonResponse response = getResponse(movie);
				response.setPhoto(base64Photo);
				responses.add(response);
			}
		}
		return ResponseEntity.ok(responses);
	}
	
	private MoviesComingSoonResponse getResponse(MoviesComingSoon comingSoon) {
		byte[] photoBytes = null;
		Blob photoBlob = comingSoon.getPhoto();
		if(photoBlob != null) {
			try {
				photoBytes = photoBlob.getBytes(1, (int)photoBlob.length());
			}
			catch(SQLException e) {
				throw new PhotoRetrievalException("Error retrieving photo");
			}
		}
		return new MoviesComingSoonResponse(
				comingSoon.getId(),
				comingSoon.getName(),
				comingSoon.getDuration(),
				comingSoon.getDistributor(),
				comingSoon.getCountry(),
				comingSoon.getYear(),
				comingSoon.getDescription(),
				comingSoon.getReleaseDate(),
				photoBytes);
	}
}
