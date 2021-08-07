package com.space.controller;

import com.space.exception.ValidationExeption;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipServiceIml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class ShipController {

    @Autowired
    private ShipServiceIml shipServiceIml;

    @RequestMapping(path = "/ships", method = RequestMethod.GET)
    public List<Ship> getAllShips(@RequestParam(name="name", required=false) String name,
                                  @RequestParam(name="planet", required=false) String planet,
                                  @RequestParam(name="shipType", required=false) ShipType shipType,
                                  @RequestParam(name="after", required=false) Long after,
                                  @RequestParam(name="before", required=false) Long before,
                                  @RequestParam(name="isUsed", required=false) Boolean isUsed,
                                  @RequestParam(name="minSpeed", required=false) Double minSpeed,
                                  @RequestParam(name="maxSpeed", required=false) Double maxSpeed,
                                  @RequestParam(name="minCrewSize", required=false) Integer minCrewSize,
                                  @RequestParam(name="maxCrewSize", required=false) Integer maxCrewSize,
                                  @RequestParam(name="minRating", required=false) Double minRating,
                                  @RequestParam(name="maxRating", required=false) Double maxRating,
                                  @RequestParam(name="order", required=false, defaultValue = "ID") ShipOrder shipOrder,
                                  @RequestParam(name="pageNumber", required=false, defaultValue = "0") Integer pageNumber,
                                  @RequestParam(name="pageSize", required=false, defaultValue = "3") Integer pageSize) {
        return shipServiceIml.getAllShips(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed,
                minCrewSize, maxCrewSize, minRating, maxRating, shipOrder, pageNumber, pageSize);
    }

    @RequestMapping(path = "/ships/count", method = RequestMethod.GET)
    public Integer getShipsCount(@RequestParam(name="name", required=false) String name,
                                 @RequestParam(name="planet", required=false) String planet,
                                 @RequestParam(name="shipType", required=false) ShipType shipType,
                                 @RequestParam(name="after", required=false) Long after,
                                 @RequestParam(name="before", required=false) Long before,
                                 @RequestParam(name="isUsed", required=false) Boolean isUsed,
                                 @RequestParam(name="minSpeed", required=false) Double minSpeed,
                                 @RequestParam(name="maxSpeed", required=false) Double maxSpeed,
                                 @RequestParam(name="minCrewSize", required=false) Integer minCrewSize,
                                 @RequestParam(name="maxCrewSize", required=false) Integer maxCrewSize,
                                 @RequestParam(name="minRating", required=false) Double minRating,
                                 @RequestParam(name="maxRating", required=false) Double maxRating) {
        return shipServiceIml.getShipsCount(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed,
                minCrewSize, maxCrewSize, minRating, maxRating);
    }

    @RequestMapping(path = "/ships", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Ship> createShip(@RequestBody Ship ship) {
        try {
            Ship shipCreated = shipServiceIml.createShip(ship);
            return new ResponseEntity<>(shipCreated, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/ships/{id}", method = RequestMethod.GET)
    public ResponseEntity<Ship> getShip(@PathVariable("id") String id) {
        try {
            Long shipId = Long.parseLong(id);
            Ship ship = shipServiceIml.getShip(shipId);
            if (ship == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(ship, HttpStatus.OK);
        } catch (NumberFormatException | ValidationExeption e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/ships/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Ship> updateShip(@PathVariable("id") String  id,
                                           @RequestBody Ship ship) {
        try {
            Long shipId = Long.parseLong(id);
            Ship shipUpdated = shipServiceIml.updateShip(shipId, ship);
            return new ResponseEntity<>(shipUpdated, HttpStatus.OK);
        } catch (NumberFormatException | ValidationExeption e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/ships/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Ship> deleteShip(@PathVariable("id") String id) {
        try {
            Long shipId = Long.parseLong(id);
            shipServiceIml.deleteShip(shipId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ValidationExeption | NumberFormatException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
