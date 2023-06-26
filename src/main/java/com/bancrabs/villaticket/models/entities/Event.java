package com.bancrabs.villaticket.models.entities;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;

@Data
@NoArgsConstructor
@Entity
@Table(name = "event", schema = "public")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "type_id", nullable = false)
    private Type type;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "start_time", nullable = true)
    private Timestamp startTime;

    @Column(name = "end_time", nullable = true)
    private Timestamp endTime;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "visibility", nullable = false)
    private Boolean isVisible;

    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @Exclude
    private List<Sponsor> sponsors;
    
    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @Exclude
    private List<Image> images;

    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @Exclude
    private List<Attendance> attendances;

    public Event(String title, Type type, Location location, Category category, Date date, Timestamp startTime, Timestamp endTime, String status, Boolean isVisible){
        this.title = title;
        this.type = type;
        this.location = location;
        this.category = category;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.isVisible = isVisible;
    }

    public Event(String title, Type type, Location location, Category category, Date date, Timestamp startTime, String status, Boolean isVisible){
        this.title = title;
        this.type = type;
        this.location = location;
        this.category = category;
        this.date = date;
        this.startTime = startTime;
        this.endTime = null;
        this.status = status;
        this.isVisible = isVisible;
    }
}
