package com.javaproject.imbd.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.Comparator;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "t_movie")
public class Movie  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  Long id;

    @Column(name = "movie_name")
    private String name;

    @Column(name = "movie_slogan")
    private String slogan;

    @Column(name = "runtime")
    private int runtime;

    @Column(name = "age")
    private int age;

    @Column(name = "ratings")
    private double ratings;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "video_url")
    private String videoUrl;


    @Column(name = "movie_release")
    private Date release;

    @ManyToMany
    private List<Genre>  genres;



    @Column(name = "country")
    private String country;



    @ManyToMany
    private List<Person>  producers ;


    @ManyToMany
    private List<Person>  writers ;

    @ManyToMany
    private List<Person>  directors ;


    @ManyToMany
    private List<Person>  actors ;




}
