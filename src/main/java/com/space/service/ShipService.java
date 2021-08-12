package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;

import java.util.List;

public interface ShipService {

    List<Ship> getAllShips(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed,
                           Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize,
                           Double minRating, Double maxRating, ShipOrder shipOrder,
                           Integer pageNumber, Integer pageSize);

    Integer getShipsCount(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed,
                       Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize,
                       Double minRating, Double maxRating);

    Ship createShip(Ship ship) throws Exception;

    Object getShip(Long id) throws Exception;

    Ship updateShip(Long id, Ship ship) throws Exception;

    void deleteShip(Long id) throws Exception;
}
