package com.metacoding.springv2.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "user_tb")
public class User implements UserDetails {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    @Column(unique = true,length = 20,nullable = false)
    private String username;
    @Column(length = 60,nullable = false)
    private String password;
    @Column(length = 30,nullable = false)
    private String email;
    private String roles; 

    @Builder
    public User(Integer id,String username, String password, String email, String roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        String[] roleList = roles.split(",");

        for (String role : roleList) {
            authorities.add(() -> "ROLE_" + role);
        }

        return authorities;
    }

}