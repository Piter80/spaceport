package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ShipServiceViaRepository implements ShipService {
    @Autowired
    private ShipRepository repository;

    @Override
    public Ship save(Ship ship) {
        if (isValidShip(ship)) return repository.save(ship);
        return null;
    }

    @Override
    public Ship getOne(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Ship update(Ship old, Ship updated) {
        return null;
    }

    @Override
    public void delete(Ship ship) {
        repository.delete(ship);
    }

    @Override
    public List<Ship> getByCriteria(
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
    ) {
        List<Ship> allShips = repository.findAll();


        Date prodDateBefore = (before == null ? null : new Date(before));
        Date prodDateAfter = (after == null ? null : new Date(after));


        return repository.findAll();
    }

    @Override
    public List<Ship> sorted(List<Ship> ships, ShipOrder order) {
        if (order == null) return  ships;
        ships.sort((o1, o2) -> {
            switch (order) {
                case ID: return o1.getId().compareTo(o2.getId());
                case RATING: return o1.getRating().compareTo(o2.getRating());
                case SPEED: return o1.getSpeed().compareTo(o2.getSpeed());
                case DATE: return o1.getProdDate().compareTo(o2.getProdDate());
                default: return 0;
            }
        });
        return ships;
    }

    @Override
    public List<Ship> getPage(List<Ship> ships, Integer number, Integer size) {
        Integer pageNumber = number == null ? 0 : number;
        Integer pageSize = size == null ? 3 : size;
        Integer firstShipToShow = pageNumber * pageSize;
        Integer lastShipToShow = firstShipToShow + pageSize;
        if (firstShipToShow > ships.size()) firstShipToShow = ships.size();
        if (lastShipToShow > ships.size()) lastShipToShow = ships.size();

        return  ships.subList(firstShipToShow, lastShipToShow);
    }

    @Override
    public double calculateRating(Double speed, Date prodDate, boolean isUsed) {
            Double used = isUsed ? 0.5 : 1.0;
            Double firstPart = 80*speed*used;
            Double secondPart = 3019 - prodDate.getYear() +1.0;
            return  Math.round(firstPart/secondPart *100) /100.0;
    }

    @Override
    public boolean isValidShip(Ship ship) {
        if (
                ship != null &&
                isValidString(ship.getName()) &&
                isValidString(ship.getPlanet()) &&
                isValidDate(ship.getProdDate()) &&
                isValidSpeed(ship.getSpeed()) &&
                isValidCrewSize(ship.getCrewSize())

        ) return true;
        return false;
    }

    private boolean isValidCrewSize(Integer crewSize) {
        if (crewSize < 1 || crewSize > 9999) return false;
        return true;
    }

    private boolean isValidSpeed(Double speed) {
        if (speed == null || speed > 0.99 || speed < 0.01) return false;
        return true;
    }

    private boolean isValidDate (Date date) {
        if (date == null || date.getYear() < 2800 || date.getYear() > 3019) return false;
        return true;
    }

    private boolean isValidString(String str) {
        if (str == null || str.length() > 50 || str.isEmpty()) return false;
        return true;
    }
}
