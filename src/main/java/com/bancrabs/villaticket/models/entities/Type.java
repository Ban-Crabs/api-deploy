package com.bancrabs.villaticket.models.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;

@Data
@NoArgsConstructor
@Entity
@Table(name = "type", schema = "public")
public class Type {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;
    
    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @Exclude
    private List<Event> events;
    
    public Type(String code, String name) {
        this.id = code;
        this.name = name;
    }

}
