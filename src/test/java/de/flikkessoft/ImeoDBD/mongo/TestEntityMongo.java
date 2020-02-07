package de.flikkessoft.ImeoDBD.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestEntityMongo {
    @Id
    private String id;
    private int age;
    private String name;
    private Float someFloat;
}
