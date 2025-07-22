package com.thukera.model.forms;

import java.util.Set;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class SignUpForm {

	@NotBlank
    @Size(min = 11, max = 15)
    private String cpf;

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

    @NotBlank
    @Size(min = 3, max = 50)
    private Long agentId;

    @NotBlank
    private Boolean status;

    @NotBlank
    @Size(min = 3, max = 50)
    private String parceiro;

    @NotBlank
    private Long id;

    private Long codigoEmpresa;

}