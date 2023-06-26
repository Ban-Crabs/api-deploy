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
@Table(name = "location", schema = "public")
public class Location {
    
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "availability", nullable = false)
    private Boolean isAvailable;

    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @Exclude
    private List<Event> events;

    public Location(String id, String name){
        this.id = id;
        this.name = name;
        this.isAvailable = true;
    }

    public Location(String id, String name, Boolean isAvailable){
        this.id = id;
        this.name = name;
        this.isAvailable = isAvailable;
    }
}
