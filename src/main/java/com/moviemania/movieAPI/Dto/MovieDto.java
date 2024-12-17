package com.moviemania.movieAPI.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {

    private Integer movieId;

    @NotBlank(message = "Please provide Movie title!")
    private String title;

    @NotBlank(message = "Please provide Movie Director!")
    private String director;

    @NotBlank(message = "Please provide Movie Studio!")
    private String studio;


    private Set<String> movieCast;

    @NotNull(message = "Please provide Movie release year!")
    private Integer releaseYear;

    @NotBlank(message = "Please provide Movie poster!")
    private String poster;

    @NotBlank(message = "Please provide poster's url!")
    private String posterUrl;
}
