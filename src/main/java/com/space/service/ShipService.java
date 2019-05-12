package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

public interface ShipService {
    Ship save(Ship ship);
    Ship getOne(Long id);
    Ship update(Ship old, Ship updated);
    void delete(Ship ship);
    List<Ship> getByCriteria(
            String name,
            String planet,
            ShipType shipType,
            Long after,
            Long before,
            Boolean isUsed,
            Double minSpeed,
            Double maxSpeed,
            Integer minCrewSize,
            Integer maxCrewSize,
            Double minRating,
            Double maxRating
    );

    List<Ship> sorted(List<Ship> ships, ShipOrder order);
    List<Ship> getPage(List<Ship> ships, Integer size, Integer number);
    double calculateRating(Double speed, Date prodDate, boolean isUsed);
    boolean isValidShip(Ship ship);
}
