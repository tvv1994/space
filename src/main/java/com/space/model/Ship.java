package com.space.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="ship")
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name", length = 50, nullable = false)
    private String name;

    @Column(name="planet", length = 50, nullable = false)
    private String planet;

    @Column(name="shipType",nullable = false)
    @Enumerated(EnumType.STRING)
    private ShipType shipType;

    @Column(name="prodDate",nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date prodDate;

    @Column(name="isUsed",nullable = false)
    private Boolean isUsed;

    @Column(name="speed", scale = 2, nullable = false)
    private Double speed;

    @Column(name="crewSize",nullable = false)
    private Integer crewSize;

    @Column(name="rating", scale = 2, nullable = false)
    private Double rating;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlanet() {
        return planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public Double getSpeed() {
        return speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public Double getRating() {
        return rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

}
