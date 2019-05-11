package com.space.controller;


import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@Controller
@RequestMapping ("/rest")
public class ShipController {
    @Autowired
    private ShipRepository shipRepository;

    @RequestMapping("/ships")
    @ResponseBody public List<Ship> findAllShips () {
        System.out.println("В контроллере" );
        return shipRepository.findAll();
    }

    @RequestMapping("/ships/count")
    @ResponseBody public int shipsCount () {
        System.out.println("В контроллере" );
        return shipRepository.findAll().size();
    }
}
