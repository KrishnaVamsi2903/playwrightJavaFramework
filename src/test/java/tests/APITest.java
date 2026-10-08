package tests;

import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

public class APITest {

    @Test
    public void e2eAPITest(){

        //login
        HashMap<Object, Object> loginPayload = new HashMap<>();
        loginPayload.put("email","krishnavamsiseereddy@gmail.com");
        loginPayload.put("password","Krishna@123");

        Playwright playwright = Playwright.create();
        APIRequestContext apiRequest =
                playwright.request().newContext(
                        new APIRequest.NewContextOptions()
                                .setIgnoreHTTPSErrors(true)
                );
        APIResponse apiResponse = apiRequest.post(
                "https://api.eventhub.rahulshettyacademy.com/api/auth/login",
                RequestOptions.create().setData(loginPayload)
        );
       // Assert.assertTrue(apiResponse.ok());
        //System.out.println(apiResponse.text());

        String token = JsonPath.read(apiResponse.text(), "$.token");
        //System.out.println("Login Successful "+token);


        //create event
        String eventTitle = "Playwright API Testing";
        HashMap<Object, Object> createEventPayload = new HashMap<>();
        createEventPayload.put("title", eventTitle);
        createEventPayload.put("description", "API Testing with Bruno App");
        createEventPayload.put("category", "Workshop");
        createEventPayload.put("venue", "JNTU");
        createEventPayload.put("city", "Hyd");
        createEventPayload.put("eventDate", "2026-10-07T12:43:00.000Z");
        createEventPayload.put("price", "50");
        createEventPayload.put("totalSeats", 100);


        APIResponse eventResponse = apiRequest.post(
                "https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer "+token)
                        .setData(createEventPayload)
        );

       /* System.out.println("Status Code : " + eventResponse.status());
        System.out.println("Create Event Response : ");
        System.out.println(eventResponse.text());

        Assert.assertTrue(eventResponse.ok(), "Create Event API is succeed");*/
        String eventID = JsonPath.read(eventResponse.text(), "$.data.id").toString();
        //System.out.println("Event created and its ID is "+eventID);


        //Get Event
        APIResponse retrieveEvents = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setQueryParam("page", "1").setQueryParam("limit", "12")
                        .setHeader("Authorization", "Bearer "+token)
                );

        /*System.out.println("Status Code : " + retrieveEvents.status());
        System.out.println("Create Event Response : ");
        System.out.println(retrieveEvents.text());*/

        Assert.assertTrue(retrieveEvents.ok(), "Event Retrieval should be succeed");

        Integer createdEventId = Integer.valueOf(eventID);
        List<Integer> allEventIDs= JsonPath.read(retrieveEvents.text(), "$.data[*].id");
        Assert.assertTrue(allEventIDs.contains(createdEventId), "Created event should be appear in event");

        //DeleteEvent

        APIResponse deleteResponse = apiRequest.delete("https://api.eventhub.rahulshettyacademy.com/api/events/"+eventID,
                RequestOptions.create().setHeader("Authorization", "Bearer "+token));

        Assert.assertTrue(deleteResponse.ok());

        //Verify deletion is success

        APIResponse retrieveEventsAfterDelete = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setQueryParam("page", "1").setQueryParam("limit", "12")
                        .setHeader("Authorization", "Bearer "+token)
        );

        List<Integer> allEventIDsAfterDelete= JsonPath.read(retrieveEventsAfterDelete.text(), "$.data[*].id");

        System.out.println(retrieveEventsAfterDelete.text());

        Assert.assertTrue(retrieveEventsAfterDelete.ok(), "Event Retrieval should be succeed");

        Assert.assertFalse(allEventIDsAfterDelete.contains(createdEventId), "Created event should not be appear in event");




    }
}
