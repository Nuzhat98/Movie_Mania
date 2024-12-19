package com.moviemania.movieAPI.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviemania.movieAPI.Dto.MovieDto;
import com.moviemania.movieAPI.Exceptions.EmptyFileException;
import com.moviemania.movieAPI.Service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/add-movie")
    public ResponseEntity<MovieDto> addMovieHandler(@RequestPart(name="file")MultipartFile file,
                                                    @RequestPart(name = "movieDto") String movieDto) throws IOException {
        if(file.isEmpty()){
            throw new EmptyFileException("The file is Empty. Please send another file.");
        }
        MovieDto dto = convertToMovieDto(movieDto);
        return new ResponseEntity<>(movieService.addMovie(dto,file), HttpStatus.CREATED);
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDto> getMovieHandler(@PathVariable(name = "movieId") Integer movieId){
        return ResponseEntity.ok(movieService.getMovie(movieId));
    }

    @PutMapping("/update/{movieId}")
    public ResponseEntity<MovieDto> updateMovieHandler(@PathVariable(name = "movieId") Integer movieId,
                                                       @RequestPart(name = "file") MultipartFile file,
                                                       @RequestPart(name = "movieDtoObj") String movieDtoObj)throws IOException{
        if(file.isEmpty()) file=null;
        MovieDto movieDto=convertToMovieDto(movieDtoObj);
        return ResponseEntity.ok(movieService.updateMovie(movieId,movieDto,file));
    }

    @DeleteMapping("/{movieId}")
    public String deleteMovieHandler(@PathVariable(name = "movieId") Integer movieId) throws IOException {
        return movieService.deleteMovie(movieId);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<MovieDto>> getAllMovieHandler(){
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    private MovieDto convertToMovieDto(String movieDtoObj) throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.readValue(movieDtoObj,MovieDto.class);
    }
}
