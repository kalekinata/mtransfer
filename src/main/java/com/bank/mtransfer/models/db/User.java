package com.bank.mtransfer.models.db;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table( name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private Date dadd;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size (max = 120)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    private Boolean active;

    public User(){}

    public User(Date dadd, String username, String email, String password, Boolean active) {
        this.dadd = dadd;
        this.username = username;
        this.email = email;
        this.password = password;
        this.active = active;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public Date getDadd() { return dadd; }

    public void setDadd(Date dadd) { this.dadd = dadd; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public Set<Role> getRoles() { return roles; }

    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public Boolean getActive() { return active; }

    public void setActive(Boolean active) { this.active = active; }
}