package com.moviemania.movieAPI.Service;

import com.moviemania.movieAPI.Dto.MovieDto;
import com.moviemania.movieAPI.Entities.Movie;
import com.moviemania.movieAPI.MovieApiApplication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface MovieService {

    MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException;

    MovieDto getMovie(Integer movieId);

    MovieDto updateMovie(Integer movieId,MovieDto movieDto,MultipartFile file)throws IOException;

    void deleteMovie(Integer movieId);

    List<MovieDto> getAllMovies();
}
