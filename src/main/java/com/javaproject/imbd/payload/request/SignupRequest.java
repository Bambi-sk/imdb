package com.javaproject.imbd.payload.request;

import com.javaproject.imbd.entities.Roles;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String fullname;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private List<Roles> roles;

    @NotBlank
    @Size(min = 4, max = 40)
    private String password;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Roles> getRole() {
        return this.roles;
    }

    public void setRole(List<Roles> role) {
        this.roles = role;
    }
}
