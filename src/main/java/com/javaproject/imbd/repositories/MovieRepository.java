package com.javaproject.imbd.repositories;

import com.javaproject.imbd.entities.Genre;
import com.javaproject.imbd.entities.Movie;
import com.javaproject.imbd.entities.Person;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {
        List<Movie> findByOrderByRatingsDesc();


        @Query(value = "SELECT m FROM Movie m WHERE  m.release>= current_date    ")
        List<Movie> findAllM();


        List<Movie> findAllByGenresId(Long id);
        List<Movie> findAllByActorsId(Long id);
        List<Movie> findAllByDirectorsId(Long id);
        List<Movie> findAllByWritersId(Long id);
        List<Movie> findAllByProducersId(Long id);
        List<Movie> findAllByNameLike(String name);
        List<Movie>findAllByActorsIn(ArrayList<Person> actors);
        List<Movie>findAllByDirectorsIn(ArrayList<Person> directors);
        List<Movie>findAllByWritersIn(ArrayList<Person> writers);

        List<Movie> findAllByGenresIn(ArrayList<Genre> genres);

        @Query(value = "select * from t_movie where year(movie_release) =?1 ",nativeQuery = true)
        List<Movie> findAllByYear(int year);

        List<Movie> findAllByGenresInAndReleaseAfterAndReleaseBefore(ArrayList<Genre> genres,Date date,Date date1);



  }
