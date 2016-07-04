package com.packtpub.mmj.booking.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.packtpub.mmj.booking.BookingApp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/**
 * Spring System test - by using @SpringApplicationConfiguration that picks up
 * same configuration that Spring Boot uses.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BookingApp.class)
@WebIntegrationTest
public class BookingControllerIntegrationTests {

    private final RestTemplate restTemplate = new TestRestTemplate();
    //Required to Generate JSON content from Java objects

    /**
     *
     */
    public static final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${local.server.port}")
    private int port;

    /**
     * Test the GET /v1/booking/{id} API
     */
    @Test
    public void testGetById() {
        //API call
        Map<String, Object> response
                = restTemplate.getForObject("http://localhost:" + port + "/v1/booking/1", Map.class);

        assertNotNull(response);

        //Asserting API Response
        String id = response.get("id").toString();
        assertNotNull(id);
        assertEquals("1", id);
        String name = response.get("name").toString();
        assertNotNull(name);
        assertEquals("Booking 1", name);
        boolean isModified = (boolean) response.get("isModified");
        assertEquals(false, isModified);
    }

    /**
     * Test the GET /v1/booking/{id} API for no content
     */
    @Test
    public void testGetById_NoContent() {

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> responseE = restTemplate.exchange("http://localhost:" + port + "/v1/booking/99", HttpMethod.GET, entity, Map.class);

        assertNotNull(responseE);

        // Should return no content as there is no booking with id 99
        assertEquals(HttpStatus.NO_CONTENT, responseE.getStatusCode());
    }

    /**
     * Test the GET /v1/booking API
     */
    @Test
    public void testGetByName() {

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("name", "Booking");
        ResponseEntity<Map[]> responseE = restTemplate.exchange("http://localhost:" + port + "/v1/booking/?name={name}", HttpMethod.GET, entity, Map[].class, uriVariables);

        assertNotNull(responseE);

        assertEquals(HttpStatus.OK, responseE.getStatusCode());
        Map<String, Object>[] responses = responseE.getBody();
        assertNotNull(responses);

        // Assumed 2+ instances exist for booking name contains word "Booking"
        assertTrue(responses.length >= 2);

        Map<String, Object> response = responses[0];
        String id = response.get("id").toString();
        assertNotNull(id);
        assertEquals("1", id);
        String name = response.get("name").toString();
        assertNotNull(name);
        assertEquals("Booking 1", name);
        boolean isModified = (boolean) response.get("isModified");
        assertEquals(false, isModified);
    }

    /**
     * Test the POST /v1/booking API
     *
     * @throws JsonProcessingException
     */
    @Test
    public void testAdd() throws JsonProcessingException {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userId", "3");
        requestBody.put("restaurantId", "1");
        requestBody.put("tableId", "1");
        LocalDate nowDate = LocalDate.now();
        LocalTime nowTime = LocalTime.now();
        requestBody.put("date", nowDate);
        requestBody.put("time", nowTime);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        objectMapper.findAndRegisterModules();
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);

        ResponseEntity<Map> responseE = restTemplate.exchange("http://localhost:" + port + "/v1/booking", HttpMethod.POST, entity, Map.class, Collections.EMPTY_MAP);

        assertNotNull(responseE);

        // Should return created (status code 201)
        assertEquals(HttpStatus.CREATED, responseE.getStatusCode());

        //validating the newly created booking using API call
        Map<String, Object> response
                = restTemplate.getForObject("http://localhost:" + port + "/v1/booking/3", Map.class);

        assertNotNull(response);

        //Asserting API Response
        String id = response.get("id").toString();
        assertNotNull(id);
        assertEquals("3", id);
        String name = response.get("name").toString();
        assertNotNull(name);
        assertEquals("Booking ".concat(id), name);
        boolean isModified = (boolean) response.get("isModified");
        assertEquals(false, isModified);
        String userId = response.get("userId").toString();
        assertNotNull(userId);
        assertEquals("3", userId);
        String restaurantId = response.get("restaurantId").toString();
        assertNotNull(restaurantId);
        assertEquals("1", restaurantId);
        String tableId = response.get("tableId").toString();
        assertNotNull(tableId);
        assertEquals("1", tableId);
        String date1 = response.get("date").toString();
        assertNotNull(date1);
        String[] arrDate = date1.split("-");
        assertEquals(nowDate, LocalDate.of(Integer.parseInt(arrDate[0].trim()),
                Integer.parseInt(arrDate[1].trim()), Integer.parseInt(arrDate[2].trim())));
        String time1 = response.get("time").toString();
        assertNotNull(time1);
        String[] arrTime = time1.split(":");
        int dotIndex = arrTime[2].indexOf(".");
        String seconds = arrTime[2].substring(0, dotIndex);
        String strMilliSeconds = arrTime[2].substring(dotIndex + 1);
        int milliSeconds = Double.valueOf(Double.valueOf(strMilliSeconds.trim()).doubleValue() * 1000000D).intValue();
        assertEquals(nowTime, LocalTime.of(Integer.parseInt(arrTime[0].trim()),
                Integer.parseInt(arrTime[1].trim()), Integer.parseInt(seconds.trim()),
                milliSeconds));
    }

}
