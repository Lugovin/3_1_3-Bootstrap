package org.example._3_1_3bootstrap.repository;



import org.example._3_1_3bootstrap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.name = :username")
    User findUserByNameWithRoles(@Param("username") String username);

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.id = :id")
    User findUserByIdWithRoles(@Param("id") Long id);

    @Query("SELECT u FROM User u JOIN FETCH u.roles")
    List<User> findAllUsersWithRoles();

}
