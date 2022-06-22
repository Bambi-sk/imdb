package com.javaproject.imbd.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "mov_reviews")
public class Reviews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  Long id;

    @Column(name = "text")
    private String text;


    @Column(name = "added_date")
    private Date AddedDate;



    @ManyToOne
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;
}
