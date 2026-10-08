package tests2;

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

public class APITest2 {

    @Test
    public void e2eAPITest(){

        HashMap<Object, Object> loginPayload = new HashMap<>();
        loginPayload.put("email", "krishnavamsiseereddy@gmail.com");
        loginPayload.put("password", "Krishna@123");

        Playwright playwright = Playwright.create();
        APIRequestContext apiRequest = playwright.request().newContext(new APIRequest.NewContextOptions().setIgnoreHTTPSErrors(true));
        APIResponse apiResponse = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/auth/login", RequestOptions.create().setData(loginPayload));
        //System.out.println(apiResponse.text());
        String token = JsonPath.read(apiResponse.text(), "$.token");
        System.out.println(token);

        //Create Event

        String eventTitle = "QA Summit BLR";
        HashMap<Object, Object> createEventPayload = new HashMap<>();
        createEventPayload.put("title", eventTitle);
        createEventPayload.put("description", "Discussion about AI integration in QA");
        createEventPayload.put("category", "Workshop");
        createEventPayload.put("city", "Bengaluru");
        createEventPayload.put("venue", "Whitefield");
        createEventPayload.put("eventDate", "2026-10-07T12:43:00.000Z");
        createEventPayload.put("price", "55");
        createEventPayload.put("totalSeats", 100);

        APIResponse apiEventCreationRes = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/events", RequestOptions.create().setHeader("Authorization", "Bearer "+token).setData(createEventPayload));
        //System.out.println(apiEventCreationRes.text());
        int eventID = JsonPath.read(apiEventCreationRes.text(), "$.data.id");
        System.out.println(eventID);


        //get Event
        APIResponse retrieveEvent = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setQueryParam("page", "1").setQueryParam("limit", "12").setHeader("Authorization", "Bearer "+token));
        //System.out.println(retrieveEvent.text());
        List<Integer> allEventIDs = JsonPath.read(retrieveEvent.text(), "$.data[*].id");
        Assert.assertTrue(allEventIDs.contains(eventID), "Event with ID "+eventID+" present in the list");

        //Delete Event
        APIResponse deleteEvent = apiRequest.delete("https://api.eventhub.rahulshettyacademy.com/api/events/"+eventID,
                RequestOptions.create().setHeader("Authorization", "Bearer "+token));

        System.out.println(deleteEvent.text());
        Assert.assertTrue(deleteEvent.ok());

        // Retrieve event after deletion

        APIResponse retrieveEventAfterDelete = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setQueryParam("page", "1").setQueryParam("limit", "12")
                        .setHeader("Authorization", "Bearer "+token));

        System.out.println(retrieveEventAfterDelete.text());
        List <Integer> allEventIDsAfterDelete = JsonPath.read(retrieveEventAfterDelete.text(), "$.data[*].id");
        Assert.assertFalse(allEventIDsAfterDelete.contains(eventID));

    }
}
