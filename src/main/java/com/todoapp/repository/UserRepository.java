package com.todoapp.repository;

import com.todoapp.model.entity.User;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CouchbaseRepository<User, String> {

    Optional<User> findByUsernameIgnoreCase(String username);

    @Query("""
            SELECT *
            FROM User u
            WHERE (:firstName IS NULL OR u.firstName LIKE :firstName)
            AND (:lastName IS NULL OR u.lastName LIKE :lastName)
            """)
    List<User> searchUser(String firstName, String lastName);

}
