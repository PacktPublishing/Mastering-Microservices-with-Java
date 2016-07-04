package com.packtpub.mmj.restaurant.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.packtpub.mmj.restaurant.RestaurantApp;
import com.packtpub.mmj.restaurant.domain.model.entity.Table;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
@SpringApplicationConfiguration(classes = RestaurantApp.class)
@WebIntegrationTest("server.port=0")
public class RestaurantControllerIntegrationTests extends
        AbstractRestaurantControllerTests {

    private final RestTemplate restTemplate = new TestRestTemplate();
    //Required to Generate JSON content from Java objects
    public static final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${local.server.port}")
    private int port;

    /**
     * Test the GET /v1/restaurants/{id} API
     */
    @Test
    public void testGetById() {
        //API call
        Map<String, Object> response
                = restTemplate.getForObject("http://localhost:" + port + "/v1/restaurants/1", Map.class);

        assertNotNull(response);

        //Asserting API Response
        String id = response.get("id").toString();
        assertNotNull(id);
        assertEquals("1", id);
        String name = response.get("name").toString();
        assertNotNull(name);
        assertEquals("Le Meurice", name);
        boolean isModified = (boolean) response.get("isModified");
        assertEquals(false, isModified);
        List<Table> tableList = (List<Table>) response.get("tables");
        assertNull(tableList);
    }

    /**
     * Test the GET /v1/restaurants/{id} API for no content
     */
    @Test
    public void testGetById_NoContent() {

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> responseE = restTemplate.exchange("http://localhost:" + port + "/v1/restaurants/99", HttpMethod.GET, entity, Map.class);

        assertNotNull(responseE);

        // Should return no content as there is no restaurant with id 99
        assertEquals(HttpStatus.NO_CONTENT, responseE.getStatusCode());
    }

    /**
     * Test the GET /v1/restaurants API
     */
    @Test
    public void testGetByName() {

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("name", "Meurice");
        ResponseEntity<Map[]> responseE = restTemplate.exchange("http://localhost:" + port + "/v1/restaurants?name={name}", HttpMethod.GET, entity, Map[].class, uriVariables);

        assertNotNull(responseE);

        // Should return no content as there is no restaurant with id 99
        assertEquals(HttpStatus.OK, responseE.getStatusCode());
        Map<String, Object>[] responses = responseE.getBody();
        assertNotNull(responses);

        // Assumed only single instance exist for restaurant name contains word "Meurice"
        assertTrue(responses.length == 1);

        Map<String, Object> response = responses[0];
        String id = response.get("id").toString();
        assertNotNull(id);
        assertEquals("1", id);
        String name = response.get("name").toString();
        assertNotNull(name);
        assertEquals("Le Meurice", name);
        boolean isModified = (boolean) response.get("isModified");
        assertEquals(false, isModified);
        List<Table> tableList = (List<Table>) response.get("tables");
        assertNull(tableList);
    }

    /**
     * Test the POST /v1/restaurants API
     *
     * @throws JsonProcessingException
     */
    @Test
    public void testAdd() throws JsonProcessingException {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "La Plaza Restaurant");
        requestBody.put("id", "11");
        requestBody.put("address", "address of La Plaza Restaurant");
        Map<String, Object> table1 = new HashMap<>();
        table1.put("name", "Table 1");
        table1.put("id", BigInteger.ONE);
        table1.put("capacity", Integer.valueOf(6));
        Map<String, Object> table2 = new HashMap<>();
        table2.put("name", "Table 2");
        table2.put("id", BigInteger.valueOf(2));
        table2.put("capacity", Integer.valueOf(4));
        Map<String, Object> table3 = new HashMap<>();
        table3.put("name", "Table 3");
        table3.put("id", BigInteger.valueOf(3));
        table3.put("capacity", Integer.valueOf(2));
        List<Map<String, Object>> tableList = new ArrayList();
        tableList.add(table1);
        tableList.add(table2);
        tableList.add(table3);
        requestBody.put("tables", tableList);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);

        ResponseEntity<Map> responseE = restTemplate.exchange("http://localhost:" + port + "/v1/restaurants", HttpMethod.POST, entity, Map.class, Collections.EMPTY_MAP);

        assertNotNull(responseE);

        // Should return created (status code 201)
        assertEquals(HttpStatus.CREATED, responseE.getStatusCode());

        //validating the newly created restaurant using API call
        Map<String, Object> response
                = restTemplate.getForObject("http://localhost:" + port + "/v1/restaurants/11", Map.class);

        assertNotNull(response);

        //Asserting API Response
        String id = response.get("id").toString();
        assertNotNull(id);
        assertEquals("11", id);
        String name = response.get("name").toString();
        assertNotNull(name);
        assertEquals("La Plaza Restaurant", name);
        String address = response.get("address").toString();
        assertNotNull(address);
        assertEquals("address of La Plaza Restaurant", address);
        boolean isModified = (boolean) response.get("isModified");
        assertEquals(false, isModified);
        List<Map<String, Object>> tableList2 = (List<Map<String, Object>>) response.get("tables");
        assertNotNull(tableList2);
        assertEquals(tableList2.size(), 3);
        tableList2.stream().forEach((table) -> {
            assertNotNull(table);
            assertNotNull(table.get("name"));
            assertNotNull(table.get("id"));
            assertTrue((Integer) table.get("capacity") > 0);
        });
    }

}
