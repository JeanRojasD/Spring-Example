package com.br.example.dto.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {

    private String username;
    private String password;
    private String email;
    private Boolean enabled;

}
