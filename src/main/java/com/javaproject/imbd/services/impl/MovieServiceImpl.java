package com.javaproject.imbd.services.impl;

import com.javaproject.imbd.entities.*;
import com.javaproject.imbd.repositories.*;
import com.javaproject.imbd.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {
    @Autowired
    private MovieRepository movieRepository;




    @Autowired
    private GenreRepository genreRepository;




    @Override
    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Movie getMovie(Long id) {
        return movieRepository.findById(id).get();
    }

    @Override
    public void deleteMovie(Movie movie) {
            movieRepository.delete(movie);
    }

    @Override
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }



    @Override
    public Genre addGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    public Genre getGenre(Long id) {
        return genreRepository.findById(id).get();
    }

    @Override
    public void deleteGenre(Genre genre) {
            genreRepository.delete(genre);
    }

    @Override
    public Genre saveGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    public List<Movie> getMoviebyRating() {
        return movieRepository.findByOrderByRatingsDesc();
    }

    @Override
    public List<Movie> getMoviebyGenre(Long id) {
        return movieRepository.findAllByGenresId(id);
    }

    @Override
    public List<Movie> getMoviebyActor(Long id) {
        return movieRepository.findAllByActorsId(id);
    }

    @Override
    public List<Movie> getMoviebyDirector(Long id) {
        return movieRepository.findAllByDirectorsId(id);
    }

    @Override
    public List<Movie> getMoviebyProducer(Long id) {
        return movieRepository.findAllByProducersId(id);
    }

    @Override
    public List<Movie> getMoviebyWriter(Long id) {
        return movieRepository.findAllByWritersId(id);
    }


    @Override
    public List<Movie> getMoviebyDate1(Date date) {
        return movieRepository.findAllM();
    }

    @Override
    public List<Movie> findAllByNameLike(String name) {
        String name1="%"+name+"%";
        return movieRepository.findAllByNameLike(name1);
    }

    @Override
    public List<Movie> getMoviesbyGenres(ArrayList<Genre> genres) {
        return movieRepository.findAllByGenresIn(genres);
    }

    @Override
    public List<Movie> getMoviesbyYear(int year) {
        return movieRepository.findAllByYear(year);
    }

    @Override
    public List<Movie> getMoviesbyGenresandYear(ArrayList<Genre> genres, Date date,Date date1) {
        return movieRepository.findAllByGenresInAndReleaseAfterAndReleaseBefore(genres,date,date1);
    }

    @Override
    public List<Movie> getMoviesbyActors(ArrayList<Person> actors) {
        return movieRepository.findAllByActorsIn(actors);

    }

    @Override
    public List<Movie> getMoviesbyDirectors(ArrayList<Person> directors) {
        return  movieRepository.findAllByDirectorsIn(directors);
    }

    @Override
    public List<Movie> getMoviesbyWriters(ArrayList<Person> writers) {
        return movieRepository.findAllByWritersIn(writers);
    }


}
