package com.packtpub.mmj.booking.domain.repository;

import com.packtpub.mmj.booking.domain.model.entity.Booking;
import com.packtpub.mmj.booking.domain.model.entity.Entity;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sourabh Sharma
 */
@Repository("bookingRepository")
public class InMemBookingRepository implements BookingRepository<Booking, String> {

    private Map<String, Booking> entities;
    private static BigInteger index = BigInteger.ZERO;

    /**
     * Initialize the in-memory Booking Repository with sample Map
     */
    public InMemBookingRepository() {
        entities = new HashMap();
        index = index.add(BigInteger.ONE);
        Booking booking = new Booking(index.toString(), "Booking ".concat(index.toString()), "1", "1", "1", LocalDate.now(), LocalTime.now());
        entities.put(index.toString(), booking);
        index = index.add(BigInteger.ONE);
        Booking booking2 = new Booking(index.toString(), "Booking ".concat(index.toString()), "2", "2", "2", LocalDate.now(), LocalTime.now());
        entities.put(index.toString(), booking2);
    }

    /**
     * Check if given booking name already exist.
     *
     * @param name
     * @return true if already exist, else false
     */
    @Override
    public boolean containsName(String name) {
        try {
            return this.findByName(name).size() > 0;
        } catch (Exception ex) {
            //Exception Handler
        }
        return false;
    }

    /**
     *
     * @param entity
     * @return
     */
    @Override
    public Booking add(Booking entity) {
        index = index.add(BigInteger.ONE);
        entity.setId(index.toString());
        entity.setName("Booking ".concat(index.toString()));
        entities.put(entity.getId(), entity);
        return entity;
    }

    /**
     *
     * @param id
     */
    @Override
    public void remove(String id) {
        if (entities.containsKey(id)) {
            entities.remove(id);
        }
    }

    /**
     *
     * @param entity
     */
    @Override
    public void update(Booking entity) {
        if (entities.containsKey(entity.getId())) {
            entities.put(entity.getId(), entity);
        }
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public boolean contains(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Entity get(String id) {
        return entities.get(id);
    }

    /**
     *
     * @return
     */
    @Override
    public Collection<Booking> getAll() {
        return entities.values();
    }

    /**
     *
     * @param name
     * @return
     * @throws Exception
     */
    @Override
    public Collection<Booking> findByName(String name) throws Exception {
        Collection<Booking> bookings = new ArrayList();
        int noOfChars = name.length();
        entities.forEach((k, v) -> {
            if (v.getName().toLowerCase().contains(name.toLowerCase().subSequence(0, noOfChars))) {
                bookings.add(v);
            }
        });
        return bookings;
    }

}
