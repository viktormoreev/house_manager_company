package org.example.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class IdGenerator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


}
