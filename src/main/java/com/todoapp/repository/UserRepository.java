package com.todoapp.repository;

import com.todoapp.model.entity.User;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CouchbaseRepository<User, String> {

    Optional<User> findByUsernameIgnoreCase(String username);

}
