package com.packtpub.mmj.mcrsrvc.domain.model;

/**
 *
 * @author Sourabh Sharma
 * @param <Restaurant>
 * @param <String>
 */
public interface RestaurantRepository<Restaurant, String> extends Repository<Restaurant, String> {

    /**
     *
     * @param name
     * @return
     */
    boolean containsName(String name);
}
