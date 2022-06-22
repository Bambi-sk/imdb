package com.javaproject.imbd.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.javaproject.imbd.entities.*;
import com.javaproject.imbd.repositories.ReviewsRepository;
import com.javaproject.imbd.repositories.RolesRepository;
import com.javaproject.imbd.repositories.UsersRepository;
import com.javaproject.imbd.services.MovieService;
import com.javaproject.imbd.services.PersonService;
import com.javaproject.imbd.services.impl.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "api/movieimdb")
public class MainRestController {
    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;


    @Autowired
    private MovieService movieService;

    @Autowired
    private PersonService personService;


    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private ReviewsRepository reviewsRepository;


    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/updateFullname")
    public ResponseEntity<?> updateFullname(@RequestBody Users users) {
        Users users1= userRepository.findById(users.getId()).get();
        users1.setFullName(users.getFullName());
        userRepository.save(users1);
        return new ResponseEntity<>(users1, HttpStatus.OK);
    }

    private  Users getUserData(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails=(UserDetailsImpl) authentication.getPrincipal();
        Users users=userRepository.findByEmail(userDetails.getEmail()).get();
        return users;
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/updatePassword")
    public String updatePassword(@RequestBody ChangePassword changePassword) {
        Users users=getUserData();
        System.out.println(users.getPassword()+"----------------"+users.getEmail());
        if(encoder.matches(changePassword.getOldpassword(),users.getPassword())){

            users.setPassword(encoder.encode(changePassword.getNewpassword()) );
            userRepository.save(users);
            return "success";
        }
        else {
            return "are not equal";
        }
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getPassword")
    public String getPassword(@PathVariable("id") long id) {
        String users1= userRepository.findById(id).get().getPassword();
        System.out.println(users1);
        return users1;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addPerson")
    public ResponseEntity<?> addPerson(@RequestBody Person person) {
       Person person1=new Person();
       person1.setBirthdate(person.getBirthdate());
       person1.setFullname(person.getFullname());
       person1.setJobs(person.getJobs());
       person1.setImgUrl(person.getImgUrl());
       personService.addPerson(person1);
        return new ResponseEntity<>(person1, HttpStatus.OK);
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addMovie")
    public ResponseEntity<?> addMovie(@RequestBody MoviesJson moviesJson) {
        Movie movie =new Movie();
        ArrayList<Person> producers = new ArrayList<>();
        for (int i=0;i<moviesJson.getProducers().size();i++){
            producers.add(personService.getPerson(moviesJson.getProducers().get(i).getId()));
        }
        ArrayList<Person> writers = new ArrayList<>();
        for (int i=0;i<moviesJson.getWriters().size();i++){
            writers.add(personService.getPerson(moviesJson.getWriters().get(i).getId()));
        }
        ArrayList<Person> actors = new ArrayList<>();
        for (int i=0;i<moviesJson.getActors().size();i++){
            actors.add(personService.getPerson(moviesJson.getActors().get(i).getId()));
        }
        ArrayList<Person> directors = new ArrayList<>();
        for (int i=0;i<moviesJson.getDirectors().size();i++){
            directors.add(personService.getPerson(moviesJson.getDirectors().get(i).getId()));
        }
        ArrayList<Genre> genres = new ArrayList<>();
        for (int i=0;i<moviesJson.getGenres().size();i++){
            genres.add(movieService.getGenre(moviesJson.getGenres().get(i).getId()));
        }

        movie.setProducers(producers);
        movie.setCountry(moviesJson.getCountry());
        movie.setAge(moviesJson.getAge());
        movie.setGenres(genres);
        movie.setImgUrl(moviesJson.getImgUrl());
        movie.setName(moviesJson.getName());
        movie.setRatings(moviesJson.getRatings());
        movie.setRuntime(moviesJson.getRuntime());
        movie.setSlogan(moviesJson.getSlogan());
        movie.setVideoUrl(moviesJson.getVideoUrl());
        movie.setDirectors(directors);
        movie.setWriters(writers);
        movie.setActors(actors);
        movie.setRelease(moviesJson.getRelease());
        Movie movie1 =new Movie();
        movie1=movieService.addMovie(movie);
        return new ResponseEntity<>( movie1,HttpStatus.OK);
    }



    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addGenre")
    public ResponseEntity<?> addGenre(@RequestBody Genre genre){
        Genre genre1=new Genre();
        genre1.setDescription(genre.getDescription());

        genre1.setName(genre.getName());
        movieService.addGenre(genre1);
        return new ResponseEntity<>( genre1,HttpStatus.OK);
    }







    @GetMapping("/getGenre/{id}")
    public ResponseEntity<?> getGenre(@PathVariable Long id){
       Genre genre=movieService.getGenre(id);
        return new ResponseEntity<>( genre,HttpStatus.OK);
    }


    @GetMapping("/getAllGenres")
    public ResponseEntity<?> getGenres(){
        List<Genre> genres=movieService.getAllGenres();
        System.out.println(genres);
        return new ResponseEntity<>( genres,HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping ("/deleteGenre/{id}")
    public ResponseEntity<?> deleteGenre(@PathVariable Long id){
        try {

            Genre genre=movieService.getGenre(id);
            System.out.println(genre+"bbb");
            movieService.deleteGenre(genre);
            return new ResponseEntity<>(genre,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/updateGenre")
    public ResponseEntity<?> updateGenre(@RequestBody Genre genre) {
        System.out.println(genre.getName());

        Genre genre1=movieService.saveGenre(genre);
        return new ResponseEntity<>(genre1, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getPerson/{id}")
    public ResponseEntity<?> getPerson(@PathVariable Long id){
        Person person=personService.getPerson(id);
        return new ResponseEntity<>(person,HttpStatus.OK);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getAllPersons")
    public ResponseEntity<?> getAllPersons(){
        List<Person> personList=personService.getAllPersons();
        return new ResponseEntity<>( personList,HttpStatus.OK);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getAllProducers")
    public ResponseEntity<?> getAllProducers(){
        List<Person> personList=personService.getAllPersonbyJob((long) 3);
        return new ResponseEntity<>( personList,HttpStatus.OK);
    }


    @GetMapping("/getAllWriters")
    public ResponseEntity<?> getAllWriters(){
        List<Person> personList=personService.getAllPersonbyJob((long) 4);
        return new ResponseEntity<>( personList,HttpStatus.OK);
    }


    @GetMapping("/getAllDirectors")
    public ResponseEntity<?> getAllDirectors(){
        List<Person> personList=personService.getAllPersonbyJob((long) 1);
        return new ResponseEntity<>( personList,HttpStatus.OK);
    }


    @GetMapping("/getAllActors")
    public ResponseEntity<?> getAllActors(){
        List<Person> personList=personService.getAllPersonbyJob((long) 2);
        return new ResponseEntity<>( personList,HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping ("/deletePerson/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable Long id){
        try {

            Person person=personService.getPerson(id);
            System.out.println(person+"bbb");
            personService.deletePerson(person);
            return new ResponseEntity<>(person,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/updatePerson")
    public ResponseEntity<?> updatePerson(@RequestBody Person person) {
        System.out.println(person.getJobs());
        ArrayList<Job> jobs= new ArrayList<Job>();
        for(int i=0;i<person.getJobs().size();i++){
            jobs.add(personService.getJob(person.getJobs().get(i).getId()));
        }
        person.setJobs(jobs);
        Person person1=personService.savePerson(person);
        return new ResponseEntity<>(person1, HttpStatus.OK);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getAllJobs")
    public ResponseEntity<?> getAllJobs(){
        List<Job> jobs=personService.getAllJobs();
        System.out.println(jobs);
        return new ResponseEntity<>( jobs,HttpStatus.OK);

    }



    @GetMapping("/getAllMovie")
    public ResponseEntity<?> getAllMovie(){
        List<Movie> movies=movieService.getAllMovies();
        System.out.println(movies);
        return new ResponseEntity<>( movies,HttpStatus.OK);

    }



    @GetMapping("/getMovie/{id}")
    public ResponseEntity<?> getMovie(@PathVariable Long id){
        Movie movie =movieService.getMovie(id);
        System.out.println(movie);
        return new ResponseEntity<>( movie,HttpStatus.OK);

    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/updateMovie")
    public ResponseEntity<?> updateMovie(@RequestBody Movie movie) {
        System.out.println(movie.getActors());
        ArrayList<Person> producers = new ArrayList<>();
        for (int i=0;i<movie.getProducers().size();i++){
            producers.add(personService.getPerson(movie.getProducers().get(i).getId()));
        }
        ArrayList<Person> writers = new ArrayList<>();
        for (int i=0;i<movie.getWriters().size();i++){
            writers.add(personService.getPerson(movie.getWriters().get(i).getId()));
        }
        ArrayList<Person> actors = new ArrayList<>();
        for (int i=0;i<movie.getActors().size();i++){
            actors.add(personService.getPerson(movie.getActors().get(i).getId()));
        }
        ArrayList<Person> directors = new ArrayList<>();
        for (int i=0;i<movie.getDirectors().size();i++){
            directors.add(personService.getPerson(movie.getDirectors().get(i).getId()));
        }
        ArrayList<Genre> genres = new ArrayList<>();
        for (int i=0;i<movie.getGenres().size();i++){
            genres.add(movieService.getGenre(movie.getGenres().get(i).getId()));
        }
        movie.setActors(actors);
        movie.setProducers(producers);
        movie.setWriters(writers);
        movie.setDirectors(directors);
        movie.setGenres(genres);
        Movie movie1=movieService.saveMovie(movie);
        return new ResponseEntity<>(movie1, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping ("/deleteMovie/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id){
        try {

            Movie movie=movieService.getMovie(id);
            System.out.println(movie+"   /movie");
            movieService.deleteMovie(movie);
            return new ResponseEntity<>(movie,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PreAuthorize("isAuthenticated()")
    @DeleteMapping ("/deleteReview/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id){
        try {

            Reviews reviews=personService.getReviews(id);
            System.out.println(reviews+"bbb");
            personService.deleteReviews(reviews);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/updateReview")
    public ResponseEntity<?> updateReview(@RequestBody Reviews reviews) {
        System.out.println(reviews.getText());
        Users users= userRepository.findById(reviews.getUser().getId()).get();
        Movie movie=movieService.getMovie(reviews.getMovie().getId());
        reviews.setUser(users);
        reviews.setMovie(movie);
        Reviews reviews1=personService.saveReviews(reviews);
        return new ResponseEntity<>(reviews1, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getReview/{id}")
    public ResponseEntity<?> getReview(@PathVariable Long id){
        Reviews reviews=personService.getReviews(id);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            String jsonInString = mapper.writeValueAsString(reviews);
            return new ResponseEntity<String>(jsonInString, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



    @GetMapping("/getAllReviews")
    public ResponseEntity<?> getAllReviews(){
        List<Reviews> reviews= reviewsRepository.findAll();
        System.out.println(reviews);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            String jsonInString = mapper.writeValueAsString(reviews);
            return new ResponseEntity<String>(jsonInString, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers(){
        List<Users> users=userRepository.findAll();
        return new ResponseEntity<>( users,HttpStatus.OK);
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addReview")
    public ResponseEntity<?> addReview(@RequestBody Reviews reviews){
        Movie movie=movieService.getMovie(reviews.getMovie().getId());
        Users users=userRepository.findById(reviews.getUser().getId()).get();
        reviews.setMovie(movie);
        reviews.setUser(users);
        long date=System.currentTimeMillis();
        Date addeddate=new Date(date);
        reviews.setAddedDate(addeddate);
        Reviews reviews1=personService.addReviews(reviews);
        return new ResponseEntity<>( reviews1,HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping ("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        try {

            Users users =userRepository.findById(id).get();
            userRepository.delete(users);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PreAuthorize("isAuthenticated()")
    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody Users users) {

       List<Roles> roles =new ArrayList<>();
       for(int i=0;i<users.getRoles().size();i++){
           roles.add(rolesRepository.findById(users.getRoles().get(i).getId()).get());
       }
        System.out.println(users.getFullName());
       users.setRoles(roles);
       userRepository.save(users);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getAllRoles")
    public ResponseEntity<?> getAllRoles(){
        List<Roles> roles=rolesRepository.findAll();
        return new ResponseEntity<>( roles,HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getUser/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id){
        Users users=userRepository.findById(id).get();
        return new ResponseEntity<>(users,HttpStatus.OK);
    }


    @GetMapping("/getAllReviewsbyMovieId/{id}")
    public ResponseEntity<?> getAllReviewsbyMovieId(@PathVariable Long id){
        List<Reviews> reviews= personService.getAllReviewsByMovie(id);
        System.out.println(reviews);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            String jsonInString = mapper.writeValueAsString(reviews);
            return new ResponseEntity<String>(jsonInString, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/getAllPopularMovie")
    public ResponseEntity<?> getAllPopularMovie(){
        List<Movie> movies=movieService.getMoviebyRating();
        System.out.println(movies);

        return new ResponseEntity<>( movies,HttpStatus.OK);

    }

    @GetMapping("/getAllReleaseMovie")
    public ResponseEntity<?> getAllReleaseMovie(){
        long date=System.currentTimeMillis();
        Date addeddate=new Date(date);
        List<Movie> movies=movieService.getMoviebyDate1(addeddate);


        return new ResponseEntity<>( movies,HttpStatus.OK);

    }
    @GetMapping("/getAllMoviebyPerson/{id}")
    public ResponseEntity<?> getAllMoviebyPerson(@PathVariable Long id){

        List<Movie> movies=movieService.getMoviebyActor(id);
        movies.addAll(movieService.getMoviebyDirector(id));
        movies.addAll(movieService.getMoviebyProducer(id));
        movies.addAll(movieService.getMoviebyWriter(id));

        return new ResponseEntity<>( movies,HttpStatus.OK);

    }
    @GetMapping("/getAllMoviebyGenre/{id}")
    public ResponseEntity<?> getAllMoviebyGenre(@PathVariable Long id){

        List<Movie> movies=movieService.getMoviebyGenre(id);

        System.out.println("///////////////"+movies+"///////////////");

        return new ResponseEntity<>( movies,HttpStatus.OK);

    }
    @GetMapping("/getAllMoviebyName/{name}")
    public ResponseEntity<?> getAllMoviebyName(@PathVariable String name){

        List<Movie> movies=movieService.findAllByNameLike(name);

        System.out.println("///////////////"+movies+"///////////////");

        return new ResponseEntity<>( movies,HttpStatus.OK);

    }
    @PostMapping("/getAllMovieAdvancedSearch")
    public ResponseEntity<?> getAllMovieAdvancedSearch(@RequestBody AdvancedSearch advancedSearch){

        ArrayList<Genre> genres=new ArrayList<>();
        List<Movie> movies= new ArrayList<>();
        ArrayList<Person> actors=new ArrayList<>();
        ArrayList<Person> directors=new ArrayList<>();
        ArrayList<Person> writers=new ArrayList<>();

        if(advancedSearch.getFromDate()!=null && advancedSearch.getGenres()==null&& advancedSearch.getToDate()==null){
            LocalDate localDate = advancedSearch.getFromDate().toLocalDate();
            LocalDate currentyear=java.time.LocalDate.now();
            System.out.println("...................."+localDate.getYear()+"................................");
            int year =localDate.getYear();
           int toyear=currentyear.getYear();
            String str=year+"-01-01";
            String str1=toyear+"-12-31";
            Date date=Date.valueOf(str);
            Date date1=Date.valueOf(str1);
            List<Movie> movies1=movieService.getAllMovies();

            for(int i=0;i<movies1.size();i++){
                if( movies1.get(i).getRelease().compareTo(date)>0){
                    movies.add(movies1.get(i));
                }
            }

        }
        else if(advancedSearch.getFromDate()==null && advancedSearch.getGenres()!=null && advancedSearch.getToDate()==null){
            for(int i=0;i<advancedSearch.getGenres().size();i++){
                genres.add(movieService.getGenre(advancedSearch.getGenres().get(i).getId()));
            }
            movies.addAll(movieService.getMoviesbyGenres(genres));
        }
        else if(advancedSearch.getFromDate()!=null && advancedSearch.getGenres()!=null&& advancedSearch.getToDate()==null){
            LocalDate localDate = advancedSearch.getFromDate().toLocalDate();
            for(int i=0;i<advancedSearch.getGenres().size();i++){
                genres.add(movieService.getGenre(advancedSearch.getGenres().get(i).getId()));
            }
            System.out.println(localDate.getYear());
            int year =localDate.getYear();
            String str=year+"-01-01";
            String str1=year+"-12-31";
            Date date=Date.valueOf(str);
            Date date1=Date.valueOf(str1);
            List<Movie> movies1=new ArrayList<>();
            System.out.println(genres);
            movies1.addAll(movieService.getMoviesbyGenres(genres));
            System.out.println(movies1);

            for(int i=0;i<movies1.size();i++){
                if(movies1.get(i).getRelease().compareTo(date)>0){
                    movies.add(movies1.get(i));
                }
            }
        }
        else if(advancedSearch.getFromDate()==null && advancedSearch.getGenres()==null&& advancedSearch.getToDate()!=null){
            LocalDate localDate = advancedSearch.getToDate().toLocalDate();
            int year =localDate.getYear();
            String str=year+"-01-01";
            String str1=year+"-12-31";
            Date date=Date.valueOf(str);
            Date date1=Date.valueOf(str1);
            List<Movie> movies1=movieService.getAllMovies();

            for(int i=0;i<movies1.size();i++){
                if( movies1.get(i).getRelease().compareTo(date1)<0){
                    movies.add(movies1.get(i));
                }
            }
        }
        else if(advancedSearch.getFromDate()!=null && advancedSearch.getGenres()==null&& advancedSearch.getToDate()!=null){
            LocalDate localDate = advancedSearch.getToDate().toLocalDate();
            int year =localDate.getYear();
            LocalDate fromdate = advancedSearch.getFromDate().toLocalDate();
            int fromyear =fromdate.getYear();
            String str=fromyear+"-01-01";
            String str1=year+"-12-31";
            Date date=Date.valueOf(str);
            Date date1=Date.valueOf(str1);
            List<Movie> movies1=movieService.getAllMovies();
            for(int i=0;i<movies1.size();i++){
                if(movies1.get(i).getRelease().compareTo(date)>0 && movies1.get(i).getRelease().compareTo(date1)<0){
                    movies.add(movies1.get(i));
                }
            }
        }
        else if(advancedSearch.getFromDate()!=null && advancedSearch.getGenres()!=null&& advancedSearch.getToDate()!=null){
            LocalDate localDate = advancedSearch.getToDate().toLocalDate();
            int year =localDate.getYear();
            LocalDate fromdate = advancedSearch.getFromDate().toLocalDate();
            int fromyear =fromdate.getYear();
            String str=fromyear+"-01-01";
            String str1=year+"-12-31";
            Date date=Date.valueOf(str);
            Date date1=Date.valueOf(str1);
            for(int i=0;i<advancedSearch.getGenres().size();i++){
                genres.add(movieService.getGenre(advancedSearch.getGenres().get(i).getId()));
            }
            List<Movie> movies1=new ArrayList<>();
            movies1.addAll(movieService.getMoviesbyGenres(genres));
            for(int i=0;i<movies1.size();i++){
                if(movies1.get(i).getRelease().compareTo(date)>0 && movies1.get(i).getRelease().compareTo(date1)<0){
                    movies.add(movies1.get(i));
                }
            }
        }
        else if(advancedSearch.getFromDate()==null && advancedSearch.getGenres()!=null&& advancedSearch.getToDate()!=null){
            LocalDate localDate = advancedSearch.getToDate().toLocalDate();
            int year =localDate.getYear();
            String str=year+"-01-01";
            String str1=year+"-12-31";
            Date date=Date.valueOf(str);
            Date date1=Date.valueOf(str1);
            List<Movie> movies1=new ArrayList<>();
            movies1.addAll(movieService.getMoviesbyGenres(genres));
            for(int i=0;i<movies1.size();i++){
                if(movies1.get(i).getRelease().compareTo(date1)<0){
                    movies.add(movies1.get(i));
                }
            }
        }

        if(advancedSearch.getActors()!=null){
            if(!movies.isEmpty()){
                List<Movie> movieList=new ArrayList<>();
                List<Movie> movies1=movies;
                for(int i=0;i<advancedSearch.getActors().size();i++){
                    actors.add(personService.getPerson(advancedSearch.getActors().get(i).getId()));
                }

                List<Movie> movies2=movieService.getMoviesbyActors(actors);
                for(int i=0;i<movies2.size();i++){
                    if(movies1.contains(movies2.get(i))){
                        movieList.add(movies2.get(i));
                    }
                }

                movies=movieList;
            }
            else {
                List<Movie> movieList=new ArrayList<>();
                List<Movie> movies1=movieService.getAllMovies();
                for(int i=0;i<advancedSearch.getActors().size();i++){
                    actors.add(personService.getPerson(advancedSearch.getActors().get(i).getId()));
                }

                List<Movie> movies2=movieService.getMoviesbyActors(actors);
                for(int i=0;i<movies2.size();i++){
                    if(movies1.contains(movies2.get(i))){
                        movieList.add(movies2.get(i));
                    }
                }

                movies=movieList;
            }

        }
        if(advancedSearch.getDirectors()!=null){
            if(!movies.isEmpty()){
                System.out.println("check movie"+"//////////");
                List<Movie> movieList=new ArrayList<>();
                List<Movie> movies1=movies;
                for(int i=0;i<advancedSearch.getDirectors().size();i++){
                    directors.add(personService.getPerson(advancedSearch.getDirectors().get(i).getId()));
                }

                List<Movie> movies2=movieService.getMoviesbyDirectors(directors);
                System.out.println(movies2);
                for(int i=0;i<movies2.size();i++){
                    if(movies1.contains(movies2.get(i))){
                        movieList.add(movies2.get(i));
                    }
                }
                System.out.println(movieList);
                movies=movieList;
                System.out.println("movies null ///////////////////////////////");
            }
            else{
                List<Movie> movieList=new ArrayList<>();
                List<Movie> movies1=movieService.getAllMovies();
                for(int i=0;i<advancedSearch.getDirectors().size();i++){
                    directors.add(personService.getPerson(advancedSearch.getDirectors().get(i).getId()));
                }

                List<Movie> movies2=movieService.getMoviesbyDirectors(directors);
                System.out.println(movies2);
                for(int i=0;i<movies2.size();i++){
                    if(movies1.contains(movies2.get(i))){
                        movieList.add(movies2.get(i));
                    }
                }
                System.out.println(movieList);
                movies=movieList;
            }

        }
        if(advancedSearch.getWriters()!=null){
            if(!movies.isEmpty()){
                List<Movie> movieList=new ArrayList<>();
                List<Movie> movies1=movies;
                System.out.println("/////////"+movies1+"////////");
                for(int i=0;i<advancedSearch.getWriters().size();i++){
                    writers.add(personService.getPerson(advancedSearch.getWriters().get(i).getId()));
                }

                List<Movie> movies2=movieService.getMoviesbyWriters(writers);
                System.out.println(movies2);
                for(int i=0;i<movies2.size();i++){
                    if(movies1.contains(movies2.get(i))){
                        System.out.println(movies2.get(i));
                        movieList.add(movies2.get(i));
                    }
                }
                System.out.println(movieList);
                movies=movieList;
            }
            else{
                List<Movie> movieList=new ArrayList<>();
                List<Movie> movies1=movieService.getAllMovies();
                System.out.println("/////////"+movies1+"////////");
                for(int i=0;i<advancedSearch.getWriters().size();i++){
                    writers.add(personService.getPerson(advancedSearch.getWriters().get(i).getId()));
                }

                List<Movie> movies2=movieService.getMoviesbyWriters(writers);
                System.out.println(movies2);
                for(int i=0;i<movies2.size();i++){
                    if(movies1.contains(movies2.get(i))){
                        System.out.println(movies2.get(i));
                        movieList.add(movies2.get(i));
                    }
                }
                System.out.println(movieList);
                movies=movieList;
            }

        }
        System.out.println("-------------------------------"+movies+"----------------------------------------");

        return new ResponseEntity<>( movies,HttpStatus.OK);

    }




}
