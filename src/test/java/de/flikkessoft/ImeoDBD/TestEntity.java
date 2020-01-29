package de.flikkessoft.ImeoDBD;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TestEntity")
public class TestEntity {
    @Id
    @GeneratedValue
    private Long id;
    private int age;
    private String name;
    private Float someFloat;
}
