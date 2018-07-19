package com.navigamez.vuepeople.repository;

import com.navigamez.vuepeople.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource
public interface PersonRepository extends MongoRepository<Person, String> {
}
