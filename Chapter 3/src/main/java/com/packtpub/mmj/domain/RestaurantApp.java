package com.packtpub.mmj.domain;

import com.packtpub.mmj.mcrsrvc.domain.model.Restaurant;
import com.packtpub.mmj.mcrsrvc.domain.model.RestaurantService;
import com.packtpub.mmj.mcrsrvc.domain.model.Table;
import com.packtpub.mmj.mcrsrvc.persistence.InMemRestaurantRepository;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author sousharm
 */
public class RestaurantApp {

    /**
     * main method of the Application
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            // Initialize the RestaurantService
            RestaurantService restaurantService = new RestaurantService(new InMemRestaurantRepository());

            // Data Creation for Restaurants
            Table table1 = new Table("Table 1", BigInteger.ONE, 6);
            Table table2 = new Table("Table 2", BigInteger.valueOf(2), 4);
            Table table3 = new Table("Table 3", BigInteger.valueOf(3), 2);
            List<Table> tableList = new ArrayList();
            tableList.add(table1);
            tableList.add(table2);
            tableList.add(table3);
            Restaurant restaurant1 = new Restaurant("Big-O Restaurant", "1", tableList);

            // Adding the created restaurant using Service
            restaurantService.add(restaurant1);

            // Note: To raise an exception give Same restaurant name to one of the below restaurant
            Restaurant restaurant2 = new Restaurant("Pizza Shops", "2", null);
            restaurantService.add(restaurant2);

            Restaurant restaurant3 = new Restaurant("La Pasta", "3", null);
            restaurantService.add(restaurant3);

            // Retrieving all restaurants using Service
            Collection<Restaurant> restaurants = restaurantService.getAll();

            // Print the retrieved restaurants on console
            System.out.println("Restaurants List:");
            restaurants.stream().forEach((restaurant) -> {
                System.out.println("Restaurant: " + restaurant);
            });
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            // Exception Handling Code
        }
    }
}
