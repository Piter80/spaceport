package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Consumer;

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
    public Ship update(Ship oldShip, Ship newShip) throws IllegalArgumentException {
        boolean shouldChangeRating = false;

        String name = newShip.getName();
        if (name != null) {
            if (isValidString(name)) {
                oldShip.setName(name);
            } else {
                throw new IllegalArgumentException();
            }
        }
        String planet = newShip.getPlanet();
        if (planet != null) {
            if (isValidString(planet)) {
                oldShip.setPlanet(planet);
            } else {
                throw new IllegalArgumentException();
            }
        }
        if (newShip.getShipType() != null) {
            oldShip.setShipType(newShip.getShipType());
        }
        Date prodDate = newShip.getProdDate();
        if (prodDate != null) {
            if (isValidDate(prodDate)) {
                oldShip.setProdDate(prodDate);
                shouldChangeRating = true;
            } else {
                throw new IllegalArgumentException();
            }
        }
        if (newShip.getUsed() != null) {
            oldShip.setUsed(newShip.getUsed());
            shouldChangeRating = true;
        }
        Double speed = newShip.getSpeed();
        if (speed != null) {
            if (isValidSpeed(speed)) {
                oldShip.setSpeed(speed);
                shouldChangeRating = true;
            } else {
                throw new IllegalArgumentException();
            }
        }
        Integer crewSize = newShip.getCrewSize();
        if (crewSize != null) {
            if (isValidCrewSize(crewSize)) {
                oldShip.setCrewSize(crewSize);
            } else {
                throw new IllegalArgumentException();
            }
        }
        if (shouldChangeRating) {
            double rating = calculateRating(oldShip.getSpeed(), oldShip.getProdDate(), oldShip.getUsed());
            oldShip.setRating(rating);
        }
        repository.save(oldShip);
        return oldShip;
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
        Date prodDateBefore = (before == null ? null : new Date(before));
        Date prodDateAfter = (after == null ? null : new Date(after));

        List<Ship> allFoundedShips = new ArrayList<>();
        repository.findAll().forEach(ship -> {
            if (name != null && !ship.getName().contains(name)) return;
            if (planet != null && !ship.getPlanet().contains(planet)) return;
            if (shipType != null && ship.getShipType() != shipType) return;
            if (after != null && ship.getProdDate().before(prodDateAfter)) return;
            if (before != null && ship.getProdDate().after(prodDateBefore)) return;
            if (isUsed != null && ship.getUsed() != isUsed) return;
            if (minSpeed != null && ship.getSpeed().compareTo(minSpeed) < 0) return;
            if (maxSpeed != null && ship.getSpeed().compareTo(maxSpeed) > 0 ) return;
            if (minCrewSize != null && ship.getCrewSize() < minCrewSize) return;
            if (maxCrewSize != null && ship.getCrewSize() > maxCrewSize) return;
            if (minRating != null && ship.getRating().compareTo(minRating) < 0) return;
            if (maxRating != null && ship.getRating().compareTo(maxRating) > 0) return;
             allFoundedShips.add(ship);
        });
        return allFoundedShips;
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
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(prodDate);
            int prodYear = calendar.get(Calendar.YEAR);
            Double secondPart = 3019 - prodYear +1.0;
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
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int yearOfCreation = calendar.get(Calendar.YEAR);
        if (date == null || yearOfCreation < 2800 || yearOfCreation > 3019) return false;
        return true;
    }

    private boolean isValidString(String str) {
        if (str == null || str.length() >= 50 || str.isEmpty()) return false;
        return true;
    }
}
