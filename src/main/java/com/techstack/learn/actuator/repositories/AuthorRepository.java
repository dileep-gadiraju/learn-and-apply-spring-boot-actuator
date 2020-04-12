package com.techstack.learn.actuator.repositories;

import com.techstack.learn.actuator.domain.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Integer> {
}
