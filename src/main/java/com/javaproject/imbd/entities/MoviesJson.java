package com.javaproject.imbd.entities;

import com.sun.source.tree.LabeledStatementTree;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MoviesJson {

    private  Long id;


    private String name;


    private String slogan;


    private int runtime;


    private int age;

    private Date release;


    private double ratings;


    private String imgUrl;


    private String videoUrl;

    private List<Genre> genres;

    private String country;

    private List<Person>  producers;

    private List<Person> writers;

    private List<Person> directors;


    private List<Person> actors;
}
