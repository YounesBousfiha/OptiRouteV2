package com.optiroute.optiroute.application.helper;


import com.optiroute.optiroute.domain.entity.Customer;
import com.optiroute.optiroute.domain.vo.Address;
import com.optiroute.optiroute.domain.vo.Coordinates;
import com.optiroute.optiroute.domain.vo.PreferredTimeSlot;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Consumer;

@Component
public class UpdateHelper {


    public <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if(value != null) setter.accept(value);
    }

    public void updateCoordinates(Customer customer, Double lat, Double lon) {
        if(null == lat && null == lon) return;
        double newLat;
        double newLon;

        Coordinates old = customer.getCoordinates();

        if(lat != null) {
            newLat = lat;
        } else if (old != null) {
            newLat = old.latitude();
        } else {
            newLat = 0;
        }

        if(lon != null) {
            newLon = lon;
        }  else if (old != null) {
            newLon = old.longitude();
        } else {
            newLon = 0;
        }

        customer.setCoordinates(new Coordinates(newLon, newLat));
    }

    public void updatePreferredTimeSlot(Customer customer, String start, String end) {
        if(null == start && null == end) return;

        PreferredTimeSlot old = customer.getPreferredTimeSlot();
        LocalDateTime newStart;
        LocalDateTime newEnd;

        if(start != null) {
            newStart = LocalDateTime.parse(start);
        } else if (old != null) {
            newStart = old.start();
        } else {
            newStart = null;
        }

        if(end != null) {
            newEnd = LocalDateTime.parse(end);
        } else if (old != null) {
            newEnd = old.end();
        } else {
            newEnd = null;
        }

        customer.setPreferredTimeSlot(new PreferredTimeSlot(newStart, newEnd));
    }

    public void updateAddress(Customer customer, String city, String country, String street, String zipCode) {
        if(null == street && null == city && null == country && null == zipCode) return;

        Address old = customer.getAddress() != null ? customer.getAddress() : new Address(null, null, null, null);

        String newStreet = street != null ? street : old.street();
        String newCity = city != null ? city : old.city();
        String newCountry = country != null ? country : old.country();
        String newZipCode = zipCode != null ? zipCode : old.postalCode();


        customer.setAddress(new Address(newCity, newCountry, newStreet, newZipCode));
    }
}
