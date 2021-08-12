package com.space.util;

import com.space.exception.BadRequestException;
import com.space.model.Ship;

import java.util.Calendar;

public class Util {

    public static Long parseId(String id) throws Exception {
        try {
            return Long.valueOf(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Некорректный ID.");
        }
    }

    public static void validateID(Long id) throws BadRequestException {
        if (id == null || id == 0 ) throw new BadRequestException("ID должен быть отличен от \"0\" и \"null\".");
    }

    public static void calculateRating(Ship ship) throws BadRequestException {
        double k = ship.getUsed() ? 0.5 : 1;
        Double speed = ship.getSpeed();

        if(speed == null) throw new BadRequestException("Некорректное значение скорости.");

        Calendar date = Calendar.getInstance();
        date.setTime(ship.getProdDate());
        int year =  date.get(Calendar.YEAR);

        double rating = (80 * ship.getSpeed() * k)/(3019 - year + 1);

        ship.setRating(Math.round(rating*100)/100D);
    }
}
