package com.projekat.cinemaApp.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projekat.cinemaApp.model.MoviesComingSoon;

public interface MovieComingSoonRepository extends JpaRepository<MoviesComingSoon, Long> {

	
	List<MoviesComingSoon> findByReleaseDateAfter(LocalDate date);
}
