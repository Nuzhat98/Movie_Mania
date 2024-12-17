package com.moviemania.movieAPI.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@Table(name = "movie")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer movieId;

    @Column(nullable = false,length = 200)
    @NotBlank(message = "Please provide Movie title!")
    private String title;

    @Column(nullable = false)
    @NotBlank(message = "Please provide Movie Director!")
    private String director;

    @Column(nullable = false)
    @NotBlank(message = "Please provide Movie Studio!")
    private String studio;

    @ElementCollection
    @CollectionTable(name = "movie_cast")
    private Set<String> movieCast;

    @Column(nullable = false)
    @NotNull(message = "Please provide Movie release year!")
    private Integer releaseYear;

    @Column(nullable = false)
    @NotBlank(message = "Please provide Movie poster!")
    private String poster;
}
