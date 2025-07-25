package com.thukera.model.forms;

import java.util.Set;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class SignUpForm {
	
	private Long id;

	@NotBlank
    @Size(min = 11, max = 15)
    private String doc;

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(max = 60)
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotNull
    private Boolean status;
    
    


}