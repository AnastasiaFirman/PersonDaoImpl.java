package org.anastasia.peopleinfoapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties("person")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "author")
    private String author;
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

}
