package br.com.alura.school.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    
    @Query("SELECT new br.com.alura.school.user.UserEnrolled(u.email, u.courses.size) from User u join u.courses group by u.email")
    List<UserEnrolled> countCoursesByEnrolledUser(); 

}

