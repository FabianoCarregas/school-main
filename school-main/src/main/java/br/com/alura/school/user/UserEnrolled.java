package br.com.alura.school.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserEnrolled {
	  
		@NotBlank
	    @Email
	    @JsonProperty
	    private final String email;
	    
		@JsonProperty(value = "quantidade_matriculas")
	    private int totalCourses;
		
	    public UserEnrolled(String email, int totalCourses) {
			this.email = email;
			this.totalCourses = totalCourses;
		} 
	    
	}
