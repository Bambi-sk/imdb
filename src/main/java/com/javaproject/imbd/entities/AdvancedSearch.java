package com.javaproject.imbd.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;

import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class AdvancedSearch {
    private Date fromDate;
    private Date toDate;
    private List<Genre> genres;
    private List<Person> actors;
    private List<Person> directors;
    private List<Person> writers;
}
