package com.packtpub.mmj.restsample.resources;

import com.packtpub.mmj.restsample.RestSampleApp;
import java.util.List;
import java.util.Map;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author sousharm
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestSampleApp.class)
@WebIntegrationTest
/**
 *
 * @author Sourabh Sharma
 */
public class CalculationControllerTest {

    private final RestTemplate restTemplate = new TestRestTemplate();

    /**
     * Test the /calculation/sqrt API
     */
    @Test
    public void testSqrtApi() {
        //API call
        Map<String, Object> response
                = restTemplate.getForObject("http://localhost:8080/calculation/sqrt/144", Map.class);

        assertNotNull(response);

        //Asserting API Response
        String function = response.get("function").toString();
        assertNotNull(function);
        assertEquals("sqrt", function);
        List inputList = (List<String>) response.get("input");
        assertNotNull(inputList);
        assertEquals(inputList.size(), 1);
        assertEquals("144", (String) inputList.get(0));
        List<String> outputList = (List<String>) response.get("output");
        assertNotNull(outputList);
        assertEquals(outputList.size(), 1);
        assertEquals("12.0", outputList.get(0));
    }

    /**
     * Test the /calculation/sqrt API for input Error
     */
    @Test
    public void testSqrtApiError() {
        //API call
        Map<String, Object> response
                = restTemplate.getForObject("http://localhost:8080/calculation/sqrt/144a", Map.class);

        assertNotNull(response);

        //Asserting API Response
        String function = response.get("function").toString();
        assertNotNull(function);
        assertEquals("sqrt", function);
        List inputList = (List<String>) response.get("input");
        assertNotNull(inputList);
        assertEquals(inputList.size(), 1);
        assertEquals("144a", (String) inputList.get(0));
        List outputList = (List<String>) response.get("output");
        assertNotNull(outputList);
        assertEquals(outputList.size(), 1);
        assertEquals("Input value is not set to numeric value.", outputList.get(0));
    }

    /**
     * Test the /calculation/power API
     */
    @Test
    public void testPowApi() {

        //Invoking the API
        Map<String, Object> response
                = restTemplate.getForObject("http://localhost:8080/calculation/power?base=2&exponent=4", Map.class);

        assertNotNull(response);

        //Asserting the response of the API.
        String function = response.get("function").toString();
        assertEquals("power", function);
        List inputList = (List<String>) response.get("input");
        assertNotNull(inputList);
        assertEquals(inputList.size(), 2);
        assertEquals("2", (String) inputList.get(0));
        assertEquals("4", (String) inputList.get(1));
        List outputList = (List<String>) response.get("output");
        assertNotNull(outputList);
        assertEquals(outputList.size(), 1);
        assertEquals("16.0", outputList.get(0));
    }

    /**
     * Test the /calculation/power API for input error
     */
    @Test
    public void testPowApiError() {

        //Invoking the API
        Map<String, Object> response
                = restTemplate.getForObject("http://localhost:8080/calculation/power?base=2a&exponent=4", Map.class);

        assertNotNull(response);

        //Asserting the response of the API.
        String function = response.get("function").toString();
        assertEquals("power", function);
        List inputList = (List<String>) response.get("input");
        assertNotNull(inputList);
        assertEquals(inputList.size(), 2);
        assertEquals("2a", (String) inputList.get(0));
        assertEquals("4", (String) inputList.get(1));
        List outputList = (List<String>) response.get("output");
        assertNotNull(outputList);
        assertEquals(outputList.size(), 1);
        assertEquals("Base or/and Exponent is/are not set to numeric value.", outputList.get(0));
    }
}
