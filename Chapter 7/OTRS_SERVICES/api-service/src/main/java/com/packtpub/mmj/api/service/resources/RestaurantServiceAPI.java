package com.packtpub.mmj.api.service.resources;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.packtpub.mmj.common.ServiceHelper;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author sousharm
 */
@RestController
@RequestMapping("/v1/restaurants")
public class RestaurantServiceAPI {

    private static final Logger LOG = LoggerFactory.getLogger(RestaurantServiceAPI.class);

    @Autowired
    ServiceHelper serviceHelper;

    //@Qualifier("userInfoRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    DiscoveryClient client;

    /**
     *
     * @param restaurantId
     * @return
     */
    @RequestMapping("/{restaurant-id}")
    @HystrixCommand(fallbackMethod = "defaultRestaurant")
    public ResponseEntity<Restaurant> getRestaurant(
            @PathVariable("restaurant-id") int restaurantId) {
        MDC.put("restaurantId", restaurantId);
        String url = "http://restaurant-service/v1/restaurants/" + restaurantId;
        LOG.debug("GetRestaurant from URL: {}", url);

        ResponseEntity<Restaurant> result = restTemplate.getForEntity(url, Restaurant.class);
        LOG.info("GetRestaurant http-status: {}", result.getStatusCode());
        LOG.debug("GetRestaurant body: {}", result.getBody());

        return serviceHelper.createResponse(result.getBody(), result.getStatusCode());
    }

    /**
     * Fetch restaurants with the specified name. A partial case-insensitive
     * match is supported. So <code>http://.../restaurants?name=rest</code> will
     * find any restaurants with upper or lower case 'rest' in their name.
     *
     * @param name
     * @return A non-null, non-empty collection of restaurants.
     */
    @RequestMapping("")
    @HystrixCommand(fallbackMethod = "defaultRestaurants")
    public ResponseEntity<Collection<Restaurant>> findByName(@RequestParam("name") String name) {
        LOG.info(String.format("api-service findByName() invoked:{} for {} ", "v1/restaurants?name=", name));
        MDC.put("restaurantId", name);
        String url = "http://restaurant-service/v1/restaurants?name=".concat(name);
        LOG.debug("GetRestaurant from URL: {}", url);
        Collection<Restaurant> restaurants;
        ResponseEntity<Collection> result = restTemplate.getForEntity(url, Collection.class);
        LOG.info("GetRestaurant http-status: {}", result.getStatusCode());
        LOG.debug("GetRestaurant body: {}", result.getBody());

        return serviceHelper.createResponse(result.getBody(), result.getStatusCode());
    }

    /**
     * Fetch all restaurants <code>http://.../restaurants/</code>
     *
     * @return A non-null, non-empty collection of restaurants.
     */
    @RequestMapping("/")
    @HystrixCommand(fallbackMethod = "defaultGetAllRestaurants")
    public ResponseEntity<Collection<Restaurant>> findAll() {
        LOG.info(String.format("api-service findAll() invoked: /v1/restaurants/"));
        String url = "http://restaurant-service/v1/restaurants/";
        LOG.debug("GetAllRestaurant from URL: {}", url);
        Collection<Restaurant> restaurants;
        ResponseEntity<Collection> result = restTemplate.getForEntity(url, Collection.class);
        LOG.info("GetRestaurant http-status: {}", result.getStatusCode());
        LOG.debug("GetRestaurant body: {}", result.getBody());

        return serviceHelper.createResponse(result.getBody(), result.getStatusCode());
    }

    /**
     * Fallback method for getProductComposite()
     *
     * @param restaurantId
     * @return
     */
    public ResponseEntity<Restaurant> defaultRestaurant(
            @PathVariable int restaurantId) {
        return serviceHelper.createResponse(null, HttpStatus.BAD_GATEWAY);
    }

    /**
     * Fallback method
     *
     * @param input
     * @return
     */
    public ResponseEntity<Collection<Restaurant>> defaultRestaurants(String input) {
        LOG.warn("Fallback method for restaurant-service is being used.");
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    /**
     * Fallback method
     *
     * @return
     */
    public ResponseEntity<Collection<Restaurant>> defaultGetAllRestaurants() {
        LOG.warn("Fallback method for restaurant-service is being used.");
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}

class Restaurant {

    private List<Table> tables = new ArrayList<>();
    private String id;
    private boolean isModified;
    private String name;
    private String address;

    /**
     *
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    public Restaurant() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIsModified() {
        return isModified;
    }

    public void setIsModified(boolean isModified) {
        this.isModified = isModified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Restaurant(String name, String id, List<Table> tables) {
        this.tables = tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    public List<Table> getTables() {
        return tables;
    }
}

class Table {

    private int capacity;

    public Table(String name, BigInteger id, int capacity) {
        this.capacity = capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }
}
