package br.com.alura.school.user;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.com.alura.school.course.Course;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Size(max=20)
    @NotBlank
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name= "user_course", joinColumns = @JoinColumn (name = "user_id"),inverseJoinColumns = @JoinColumn(name = "course_id"))
    public Set<Course> courses = new HashSet<>();
    
    @Deprecated
    protected User() {}

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    String getUsername() {
        return username;
    }

    String getEmail() {
        return email;
    }

	public Set<Course> getCourses() {
		return courses;
	}

}
