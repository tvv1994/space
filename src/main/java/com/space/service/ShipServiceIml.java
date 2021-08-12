package com.space.service;

import com.space.controller.ShipOrder;
import com.space.exception.*;
import com.space.model.*;
import com.space.repository.ShipRepository;
import com.space.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class ShipServiceIml implements ShipService{

    @Autowired
    private ShipRepository shipRepository;

    @Override
    public List<Ship> getAllShips(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed,
                                  Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize,
                                  Double minRating, Double maxRating, ShipOrder shipOrder,
                                  Integer pageNumber, Integer pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, shipOrder.getFieldName()));

        Specification<Ship> specification = Specification.where(selectByName(name)
                .and(selectByPlanet(planet))
                .and(selectByShipType(shipType))
                .and(selectByProdDate(after, before))
                .and(selectByUse(isUsed))
                .and(selectBySpeed(minSpeed, maxSpeed))
                .and(selectByCrewSize(minCrewSize, maxCrewSize))
                .and(selectByRating(minRating, maxRating)));

        List<Ship> ships = shipRepository.findAll(specification, page).getContent();

        return ships;
    }

    @Override
    public Integer getShipsCount(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed,
                                 Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize,
                                 Double minRating, Double maxRating) {

        Specification<Ship> specification = Specification.where(selectByName(name)
                .and(selectByPlanet(planet))
                .and(selectByShipType(shipType))
                .and(selectByProdDate(after, before))
                .and(selectByUse(isUsed))
                .and(selectBySpeed(minSpeed, maxSpeed))
                .and(selectByCrewSize(minCrewSize, maxCrewSize))
                .and(selectByRating(minRating, maxRating)));

        return shipRepository.findAll(specification).size();
    }

    @Override
    public Ship createShip(Ship ship) throws Exception {

        validateData(ship);
        if (ship.getUsed() == null) ship.setUsed(false);
        Util.calculateRating(ship);

        shipRepository.save(ship);
        return ship;
    }

    @Override
    public Ship getShip(Long id) throws Exception {
        Ship ship = null;
        Util.validateID(id);
        try {
            ship = shipRepository.findById(id).get();
        } catch (Exception e) {
            throw new NotFoundException(String.format("Корабль c ID: %d отсутствует.", id));
        }
        return ship;
    }

    @Override
    public Ship updateShip(Long id, Ship ship) throws Exception {
            validateData(ship);

            Ship oldShip = getShip(id);

            if (ship.getName() != null && !ship.getName().isEmpty()) oldShip.setName(ship.getName());
            if (ship.getPlanet() != null) oldShip.setPlanet(ship.getPlanet());
            if (ship.getShipType() != null) oldShip.setShipType(ship.getShipType());
            if (ship.getProdDate() != null) oldShip.setProdDate(ship.getProdDate());
            if (ship.getUsed() != null) oldShip.setUsed(ship.getUsed());
            if (ship.getSpeed() != null) oldShip.setSpeed(ship.getSpeed());
            if (ship.getCrewSize() != null) oldShip.setCrewSize(ship.getCrewSize());

            Util.calculateRating(oldShip);
            shipRepository.save(oldShip);
            return oldShip;
    }

    @Override
    public void deleteShip(Long id) throws Exception {
            Util.validateID(id);
        try {
            shipRepository.deleteById(id);
        } catch (Exception e) {
            throw new NotFoundException(String.format("Ошибка при удалении корабля с ID: %d.", id));
        }
    }

    private Specification<Ship> selectByName(String name) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (name == null) {
                    return null;
                }
                return criteriaBuilder.like(root.get("name"), "%" + name + "%");
            }
        };
    }

    private Specification<Ship> selectByPlanet(String planet) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (planet == null) {
                    return null;
                }
                return criteriaBuilder.like(root.get("planet"), "%" + planet + "%");
            }
        };
    }

    public Specification<Ship> selectByShipType(ShipType shipType) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (shipType == null) {
                    return null;
                }
                return criteriaBuilder.equal(root.get("shipType"), shipType);
            }
        };
    }

    private Specification<Ship> selectByProdDate(Long after, Long before) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (after == null && before == null) {
                    return null;
                }

                if (after == null) {
                    Date tempBefore = new Date(before);
                    return criteriaBuilder.lessThanOrEqualTo(root.get("prodDate"), tempBefore);
                }

                if (before == null) {
                    Date tempAfter = new Date(after);
                    return criteriaBuilder.greaterThanOrEqualTo(root.get("prodDate"), tempAfter);
                }

                Calendar beforeCalendar = new GregorianCalendar();
                beforeCalendar.setTime(new Date(before));
                beforeCalendar.set(Calendar.HOUR, 0);
                beforeCalendar.add(Calendar.MILLISECOND, -1);

                Date tempAfter = new Date(after);
                Date tempBefore = beforeCalendar.getTime();

                return criteriaBuilder.between(root.get("prodDate"), tempAfter, tempBefore);
            }
        };
    }

    private Specification<Ship> selectByUse(Boolean isUsed) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (isUsed == null) {
                    return null;
                }
                if (isUsed) {
                    return criteriaBuilder.isTrue(root.get("isUsed"));
                } else {
                    return criteriaBuilder.isFalse(root.get("isUsed"));
                }
            }
        };
    }

    private Specification<Ship> selectBySpeed(Double minSpeed, Double maxSpeed) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (minSpeed == null && maxSpeed == null) {
                    return null;
                }
                if (minSpeed == null) {
                    return criteriaBuilder.lessThanOrEqualTo(root.get("speed"), maxSpeed);
                }
                if (maxSpeed == null) {
                    return criteriaBuilder.greaterThanOrEqualTo(root.get("speed"), minSpeed);
                }
                return criteriaBuilder.between(root.get("speed"), minSpeed, maxSpeed);
            }
        };
    }

    private Specification<Ship> selectByCrewSize(Integer minCrewSize, Integer maxCrewSize) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (minCrewSize == null && maxCrewSize == null) {
                    return null;
                }
                if (minCrewSize == null) {
                    return criteriaBuilder.lessThanOrEqualTo(root.get("crewSize"), maxCrewSize);
                }
                if (maxCrewSize == null) {
                    return criteriaBuilder.greaterThanOrEqualTo(root.get("crewSize"), minCrewSize);
                }
                return criteriaBuilder.between(root.get("crewSize"), minCrewSize, maxCrewSize);
            }
        };
    }

    public Specification<Ship> selectByRating(Double minRating, Double maxRating) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (minRating == null && maxRating == null) {
                    return null;
                }
                if (minRating == null) {
                    return criteriaBuilder.lessThanOrEqualTo(root.get("rating"), maxRating);
                }
                if (maxRating == null) {
                    return criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), minRating);
                }
                return criteriaBuilder.between(root.get("rating"), minRating, maxRating);
            }
        };
    }

    private void validateData(Ship ship) throws BadRequestException {

        Integer year = null;
        Long id = ship.getId();

        if (ship.getProdDate() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(ship.getProdDate());
            year = calendar.get(Calendar.YEAR);
        }

        if ((ship.getName() != null && (ship.getName().length() > 50 || ship.getName().length() == 0)) ||
                (ship.getPlanet() != null && ship.getPlanet().length() > 50) ||
                (year != null && (year < 2800 || year > 3019)) ||
                (ship.getSpeed() != null && (ship.getSpeed() < 0.01 || ship.getSpeed() > 0.99)) ||
                (ship.getCrewSize() != null && (ship.getCrewSize() <1 || ship.getCrewSize() > 9999))
        ) throw new BadRequestException("Ошибка в данных.");
    }
}
