package com.space.model;


import com.space.model.ShipType;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name="ship")
public class Ship {
    private static int currentYear = 3019;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name")
    private String name;
    @Column(name="planet")
    private String planet;
    @Enumerated(EnumType.STRING)
    @Column (name = "shipType")
    private ShipType shipType;
    @Column(name="prodDate")
    Date prodDate;
    @Column(name="isUsed")
    Boolean isUsed;
    @Column(name="speed")
    Double speed;
    @Column(name="crewSize")
    Integer crewSize;
    @Column(name="rating")
    Double rating;

    public static int getCurrentYear() {
        return currentYear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    private Double getCurrentRating() {
        Double isUsed = (this.isUsed) ? 0.5 : 1.0;
        Double firstPart = 80*this.speed*isUsed;
        Double secondPart = currentYear - this.prodDate.getYear() +1.0;
        return  firstPart/secondPart;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", planet='" + planet + '\'' +
                ", shipType=" + shipType +
                ", prodDate=" + prodDate +
                ", isUsed=" + isUsed +
                ", speed=" + speed +
                ", crewSize=" + crewSize +
                ", rating=" + rating +
                '}';
    }
}
