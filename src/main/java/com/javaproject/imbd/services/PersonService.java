package com.javaproject.imbd.services;

import com.javaproject.imbd.entities.Job;
import com.javaproject.imbd.entities.Movie;
import com.javaproject.imbd.entities.Person;
import com.javaproject.imbd.entities.Reviews;

import java.util.List;

public interface PersonService {

    Job addJob(Job job);
    List<Job> getAllJobs();
    Job getJob(Long id);
    void deleteJob(Job job);
    Job saveJob(Job job);


    Person addPerson(Person person);
    List<Person> getAllPersons();
    Person getPerson(Long id);
    void deletePerson(Person person);
    Person savePerson(Person person);

    List<Person> getAllPersonbyJob(Long id);


    Reviews addReviews(Reviews reviews);
    List<Reviews> getAllReviews();
    Reviews getReviews(Long id);
    void deleteReviews(Reviews reviews);
    Reviews saveReviews(Reviews reviews);

    List<Reviews> getAllReviewsByMovie(Long id);

}
