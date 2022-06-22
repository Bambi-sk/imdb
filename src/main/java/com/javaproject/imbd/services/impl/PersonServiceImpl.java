package com.javaproject.imbd.services.impl;

import com.javaproject.imbd.entities.Job;
import com.javaproject.imbd.entities.Person;
import com.javaproject.imbd.entities.Reviews;
import com.javaproject.imbd.repositories.JobRepository;
import com.javaproject.imbd.repositories.MovieRepository;
import com.javaproject.imbd.repositories.PersonRepository;
import com.javaproject.imbd.repositories.ReviewsRepository;
import com.javaproject.imbd.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private JobRepository jobRepository;


    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ReviewsRepository reviewsRepository;


    @Override
    public Job addJob(Job job) {
        return jobRepository.save(job);
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public Job getJob(Long id) {
        return jobRepository.findById(id).get();
    }

    @Override
    public void deleteJob(Job job) {
                jobRepository.delete(job);
    }

    @Override
    public Job saveJob(Job job) {
        return  jobRepository.save(job);
    }

    @Override
    public Person addPerson(Person person) {
        return personRepository.save(person);
    }

    @Override
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @Override
    public Person getPerson(Long id) {
        return personRepository.findById(id).get();
    }

    @Override
    public void deletePerson(Person person) {
                personRepository.delete(person);
    }

    @Override
    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    @Override
    public List<Person> getAllPersonbyJob(Long id) {
        return personRepository.findAllByJobsId(id);
    }

    @Override
    public Reviews addReviews(Reviews reviews) {
        return reviewsRepository.save(reviews);
    }

    @Override
    public List<Reviews> getAllReviews() {
        return reviewsRepository.findAll();
    }

    @Override
    public Reviews getReviews(Long id) {
        return reviewsRepository.findById(id).get();
    }

    @Override
    public void deleteReviews(Reviews reviews) {
                reviewsRepository.delete(reviews);
    }

    @Override
    public Reviews saveReviews(Reviews reviews) {
        return reviewsRepository.save(reviews);
    }

    @Override
    public List<Reviews> getAllReviewsByMovie(Long id) {
        return reviewsRepository.findAllByMovieId(id);
    }
}
