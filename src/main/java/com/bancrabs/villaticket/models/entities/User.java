package com.bancrabs.villaticket.models.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user", schema = "public")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password" , nullable = true)
    private String password;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "active", insertable = false)
    private Boolean active;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    @Exclude
    private List<Token> tokens;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    @JsonIgnore
    @Exclude
    private List<UserPrivilege> privileges;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      ArrayList<GrantedAuthority> authorities = new ArrayList<>();
      privileges.forEach(priv->{
        authorities.add(new GrantedAuthority(){
          @Override
          public String getAuthority() {
            return priv.getName();
          }
        });
      });
      return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
      return this.active;
    }

    @Override
    public boolean isAccountNonLocked() {
      return this.active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
      return this.active;
    }

    @Override
    public boolean isEnabled() {
      return this.active;
    }

    public User(String unStr, String emStr, String pwStr) {
        this.username = unStr;
        this.password = pwStr;
        this.email = emStr;
    }
}
