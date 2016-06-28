package com.packtpub.mmj.mcrsrvc.domain.model;

import java.math.BigInteger;
import java.util.Collection;

/**
 *
 * @author Sourabh Sharma
 */
public class RestaurantService extends BaseService<Restaurant, BigInteger> {

    private RestaurantRepository<Restaurant, String> restaurantRepository;

    /**
     *
     * @param repository
     */
    public RestaurantService(RestaurantRepository repository) {
        super(repository);
        restaurantRepository = repository;
    }

    /**
     *
     * @param restaurant
     * @throws Exception
     */
    @Override
    public void add(Restaurant restaurant) throws Exception {
        if (restaurantRepository.containsName(restaurant.getName())) {
            throw new Exception(String.format("There is already a product with the name - %s", restaurant.getName()));
        }

        if (restaurant.getName() == null || "".equals(restaurant.getName())) {
            throw new Exception("Restaurant name cannot be null or empty string.");
        }
        super.add(restaurant);
    }

    /**
     *
     * @return
     */
    @Override
    public Collection<Restaurant> getAll() {
        return super.getAll();
    }
}
