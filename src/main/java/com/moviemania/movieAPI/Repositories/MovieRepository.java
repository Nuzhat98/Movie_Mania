package com.moviemania.movieAPI.Repositories;

import com.moviemania.movieAPI.Entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie,Integer> {
}
