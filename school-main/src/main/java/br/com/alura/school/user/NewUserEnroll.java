package br.com.alura.school.user;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class NewUserEnroll {

	@Size(max=20)
    @NotBlank
    @JsonProperty
    private String username;

	@Deprecated
	public NewUserEnroll() {
	}
	
    public NewUserEnroll(@Size(max = 20) @NotBlank String username) {
		super();
		this.username = username;
	}

	public String getUsername() {
        return username;
    }

	public void setUsername(String username) {
		this.username = username;
	}
    
}
