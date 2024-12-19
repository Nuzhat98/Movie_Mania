package com.moviemania.movieAPI.Service;

import com.moviemania.movieAPI.Dto.MovieDto;
import com.moviemania.movieAPI.Dto.MoviePageResponse;
import com.moviemania.movieAPI.Entities.Movie;
import com.moviemania.movieAPI.Exceptions.FileExistsException;
import com.moviemania.movieAPI.Exceptions.MovieNotFoundException;
import com.moviemania.movieAPI.Repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;
    private final FileService fileService;

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
    }


    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
        //1.upload
        if(Files.exists(Paths.get(path+ File.separator+file.getOriginalFilename()))){
            throw new FileExistsException("File already Exists! Enter another filename.");
        }

        String uploadedFileName= fileService.uploadFile(path,file);
        //2.set value of field "poster" as filename
        movieDto.setPoster(uploadedFileName);
        //3.map dto to movie obj
        Movie movie=new Movie(
                null,
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster());
        //4.save movie
        Movie savedMovie=movieRepository.save(movie);
        //5.generate poster url
        String posterUrl=baseUrl +"/file/"+uploadedFileName;
        //6.return
        MovieDto response=new MovieDto(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                posterUrl);
        return response;
    }




    @Override
    public MovieDto getMovie(Integer movieId) {
        //1.check if exists and fetch
        Movie movie=movieRepository.findById(movieId).orElseThrow(()->new MovieNotFoundException("Movie not found with id "+movieId));
        //2.generate poster
        String posterUrl=baseUrl +"/file/"+movie.getPoster();
        //3.map MovieDto obj
        MovieDto response=new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl);
        return response;
    }

    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException {
        //1.Check if exists
        Movie movie=movieRepository.findById(movieId).orElseThrow(()->new MovieNotFoundException("Movie not found with id "+movieId));
        //2.if file is null or not
        String fileName=movie.getPoster();
        if(file!=null){
            Files.deleteIfExists(Paths.get(path + File.separator + fileName));
            fileName=fileService.uploadFile(path,file);
        }
        //3.set movieDto poster value
        movieDto.setPoster(fileName);
        //4.movie obj and save
        Movie newMovie =new Movie(
               movieDto.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );
        Movie updatedMovie=movieRepository.save(newMovie);
        //4.posterUrl
        String posterUrl=baseUrl+"/file"+fileName;

        //5.return
        MovieDto response=new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl);
        return response;

    }

    @Override
    public String deleteMovie(Integer movieId) throws IOException {
        Movie movie = movieRepository.findById(movieId)
                        .orElseThrow(()->new MovieNotFoundException("Movie not found with id "+movieId));
        Integer id=movie.getMovieId();

        Files.deleteIfExists(Paths.get(path+File.separator+movie.getPoster()));
        movieRepository.delete(movie);
        return "Movie "+ id +" deleted successfully!";
    }



    @Override
    public List<MovieDto> getAllMovies() {
        //1.fetch all data
        List<Movie> movies=movieRepository.findAll();


        //2.iterate and generate poster
        List<MovieDto> movieDtos=movies.stream().map(movie -> {
            String posterUrl=baseUrl +"/file/"+movie.getPoster();
            MovieDto response=new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl);
            return response;
        }).collect(Collectors.toList());
        //
        return movieDtos;
    }

    @Override
    public MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable=  PageRequest.of(pageNumber,pageSize);

        Page<Movie> moviePages=movieRepository.findAll(pageable);

        List<Movie> movies= moviePages.getContent();
        List<MovieDto> movieDtos=movies.stream().map(movie -> {
            String posterUrl=baseUrl +"/file/"+movie.getPoster();
            MovieDto response=new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl);
            return response;
        }).collect(Collectors.toList());
        return new MoviePageResponse(movieDtos,
                pageNumber,
                pageSize,
                moviePages.getTotalElements(),
                moviePages.getTotalPages(),
                moviePages.isLast());
    }

    @Override
    public MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber,
                                                                  Integer pageSize,
                                                                  String sortBy,
                                                                  String dir) {
        Sort sort = dir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageable=  PageRequest.of(pageNumber,pageSize,sort);

        Page<Movie> moviePages=movieRepository.findAll(pageable);

        List<Movie> movies= moviePages.getContent();
        List<MovieDto> movieDtos=movies.stream().map(movie -> {
            String posterUrl=baseUrl +"/file/"+movie.getPoster();
            MovieDto response=new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl);
            return response;
        }).collect(Collectors.toList());
        return new MoviePageResponse(movieDtos,
                pageNumber,
                pageSize,
                moviePages.getTotalElements(),
                moviePages.getTotalPages(),
                moviePages.isLast());
    }
}
