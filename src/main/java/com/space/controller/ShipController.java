package com.space.controller;


import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import com.space.service.ShipService;
import com.space.service.ShipServiceViaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/rest")
public class ShipController {
    @Autowired
    private ShipService service;


    @RequestMapping(value = "/ships", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Ship> findAll(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "planet", required = false) String planet,
            @RequestParam(value = "shipType", required = false) ShipType shipType,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "isUsed", required = false) Boolean isUsed,
            @RequestParam(value = "minSpeed", required = false) Double minSpeed,
            @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "maxRating", required = false) Double maxRating,
            @RequestParam(value = "order", required = false) ShipOrder order,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) {
        List<Ship> ships = service.getByCriteria(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
        List<Ship> sortedShips = service.sorted(ships, order);
        return service.getPage(sortedShips, pageNumber, pageSize);
    }

    @RequestMapping(value = "/ships/count", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Integer shipsCount(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "planet", required = false) String planet,
            @RequestParam(value = "shipType", required = false) ShipType shipType,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "isUsed", required = false) Boolean isUsed,
            @RequestParam(value = "minSpeed", required = false) Double minSpeed,
            @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "maxRating", required = false) Double maxRating
    ) {
        return service.getByCriteria(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating).size();
    }

    @RequestMapping(path = "/ships", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Ship> createShip(@RequestBody Ship ship) {
        if (!service.isValidShip(ship)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (ship.getUsed() == null) ship.setUsed(false);
        double rating = service.calculateRating(ship.getSpeed(), ship.getProdDate(), ship.getUsed());
        ship.setRating(rating);
        Ship savedShip = service.save(ship);
        if (savedShip == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(savedShip, HttpStatus.OK);
    }

    @RequestMapping(path = "/ships/{id}", method = RequestMethod.GET)
    public ResponseEntity<Ship> getShip(@PathVariable(value = "id") String id) {
        Long convertedId = convertToLong(id);
        if (convertedId == null || convertedId <= 0)  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Ship ship = service.getOne(convertedId);
        if (ship == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(ship, HttpStatus.OK);
    }

    @RequestMapping(path = "/ships/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Ship> updateShip(
            @PathVariable(value = "id") String id,
            @RequestBody Ship ship
    ) {
        ResponseEntity<Ship> shipToUpdate = getShip(id);
        Ship shipForSave = shipToUpdate.getBody();
        if (shipForSave == null) return shipToUpdate;
        Ship savedShip;
        try {
            savedShip = service.update(shipForSave, ship);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(savedShip, HttpStatus.OK);
    }

    @RequestMapping(path = "/ships/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Ship> deleteShip(@PathVariable(value = "id") String id) {
        Long idConverted = convertToLong(id);
        if (idConverted == null || idConverted == 0)  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        ResponseEntity<Ship> ship = getShip(id);
        Ship  shipForDel= ship.getBody();
        if (shipForDel == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        service.delete(shipForDel);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Long convertToLong (String id) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
