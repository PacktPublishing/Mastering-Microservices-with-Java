package com.packtpub.mmj.restaurant.domain.service;

import com.packtpub.mmj.restaurant.domain.model.entity.Entity;
import com.packtpub.mmj.restaurant.domain.model.entity.Restaurant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author Sourabh Sharma
 */
public interface RestaurantService {

    /**
     *
     * @param restaurant
     * @throws Exception
     */
    public void add(Restaurant restaurant) throws Exception;

    /**
     *
     * @param restaurant
     * @throws Exception
     */
    public void update(Restaurant restaurant) throws Exception;

    /**
     *
     * @param id
     * @throws Exception
     */
    public void delete(String id) throws Exception;

    /**
     *
     * @param restaurantId
     * @return
     * @throws Exception
     */
    public Entity findById(String restaurantId) throws Exception;

    /**
     *
     * @param name
     * @return
     * @throws Exception
     */
    public Collection<Restaurant> findByName(String name) throws Exception;

    /**
     *
     * @param name
     * @return
     * @throws Exception
     */
    public Collection<Restaurant> findByCriteria(Map<String, ArrayList<String>> name) throws Exception;
}
