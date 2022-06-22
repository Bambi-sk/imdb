package com.javaproject.imbd.services;

import com.javaproject.imbd.entities.*;

import javax.xml.catalog.Catalog;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public interface MovieService {
    Movie addMovie(Movie movie);
    List<Movie> getAllMovies();
    Movie getMovie(Long id);
    void deleteMovie(Movie movie);
    Movie saveMovie(Movie movie);
    Genre addGenre(Genre Genre);
    List<Genre> getAllGenres();
    Genre getGenre(Long id);
    void deleteGenre(Genre genre);
    Genre saveGenre(Genre genre);

    List<Movie> getMoviebyRating();
    List<Movie> getMoviebyGenre(Long id);

    List<Movie> getMoviebyActor(Long id);
    List<Movie> getMoviebyDirector(Long id);
    List<Movie> getMoviebyProducer(Long id);
    List<Movie> getMoviebyWriter(Long id);
    List<Movie> getMoviebyDate1(Date date);
    List<Movie> findAllByNameLike(String name);
    List<Movie> getMoviesbyGenres(ArrayList<Genre> genres);
    List<Movie> getMoviesbyYear(int year);
    List<Movie> getMoviesbyGenresandYear(ArrayList<Genre> genres,Date date,Date date1);
    List<Movie> getMoviesbyActors(ArrayList<Person> actors);
    List<Movie> getMoviesbyDirectors(ArrayList<Person> directors);
    List<Movie> getMoviesbyWriters(ArrayList<Person> writers);

}
