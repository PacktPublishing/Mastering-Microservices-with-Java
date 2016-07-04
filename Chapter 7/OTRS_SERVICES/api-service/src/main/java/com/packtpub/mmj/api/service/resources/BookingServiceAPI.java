package com.packtpub.mmj.api.service.resources;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.packtpub.mmj.common.ServiceHelper;
import java.time.LocalDate;
import java.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author sousharm
 */
@RestController
@RequestMapping("/v1/bookings/")
public class BookingServiceAPI {

    private static final Logger LOG = LoggerFactory.getLogger(BookingServiceAPI.class);

    @Autowired
    ServiceHelper serviceHelper;

    //@Qualifier("userInfoRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    DiscoveryClient client;

    /**
     * Add booking with the specified information.
     *
     * @param booking
     * @return A non-null booking.
     */
    @RequestMapping(method = RequestMethod.POST)
    @HystrixCommand(fallbackMethod = "defaultAddBooking")
    public ResponseEntity<Booking> addBooking(@RequestBody Booking booking) {
        LOG.info(String.format("api-service addBooking() invoked: POST /v1/booking/"));
        String url = "http://booking-service/v1/booking/";
        LOG.debug("addBooking from URL: {}", url);
        ResponseEntity<Booking> result = restTemplate.postForEntity(url, booking, Booking.class);
        LOG.info("addBooking http-status: {}", result.getStatusCode());
        LOG.debug("addBooking body: {}", result.getBody());

        return serviceHelper.createResponse(result.getBody(), result.getStatusCode());
    }

    /**
     * Fallback method
     *
     * @param booking
     * @return
     */
    public ResponseEntity<Booking> defaultAddBooking(Booking booking) {
        LOG.warn("Fallback method for booking-service is being used.");
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}

class Booking {

    private String name;
    private String id;
    private String restaurantId;
    private String userId;
    private LocalDate date;

    private LocalTime time;
    private String tableId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getTableId() {
        return tableId;
    }

    /**
     *
     * @param tableId
     */
    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    /**
     *
     * @return
     */
    public String getRestaurantId() {
        return restaurantId;
    }

    /**
     *
     * @param restaurantId
     */
    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    /**
     *
     * @return
     */
    public String getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     *
     * @param date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     *
     * @return
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     *
     * @param time
     */
    public void setTime(LocalTime time) {
        this.time = time;
    }

    /**
     * Overridden toString() method that return String presentation of the
     * Object
     *
     * @return
     */
    @Override
    public String toString() {
        return new StringBuilder("{id: ").append(id).append(", name: ")
                .append(name).append(", userId: ").append(userId)
                .append(", restaurantId: ").append(restaurantId)
                .append(", tableId: ").append(tableId)
                .append(", date: ").append(date).append(", time: ").append(time).append("}").toString();
    }
}
