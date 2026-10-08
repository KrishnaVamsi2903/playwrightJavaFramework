package tests;

import com.microsoft.playwright.Locator;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;
import utils.DataProviderUtil;

import java.io.IOException;
import java.util.HashMap;

public class FrameworkBuildDataDrivenTest extends TestBase{

    @DataProvider(name = "eventBookingData")
    public Object[][] eventBookingData() throws IOException {
        return DataProviderUtil.getJsonDataToMap("/src/test/resources/eventBookingData.json");
    }

    @Test(groups = {"framework"}, dataProvider = "eventBookingData", description = "Create an Event - Book that event and verify if it's booked")
    public void DemoTest(HashMap<String, String> data){

        LoginPage loginPage = new LoginPage(page, base_url);
        DashboardPage dashboardPage = loginPage.loginToApplication();
        dashboardPage.waitForEventsToLoad();
        AdminEventsPage adminEventsPage = new AdminEventsPage(page);
        adminEventsPage.navigateToEventsPage();
        adminEventsPage.createEvent(data.get("titlePrefix"), data.get("description"), data.get("category"), data.get("city"),
                data.get("venue"), data.get("dateTime"), data.get("price"), data.get("totalSeats")
        );

        //step-2
        EventsPage eventsPage = new EventsPage(page);
        eventsPage.goTo();
        Locator targetLocator = eventsPage.findEventCard(data.get("titlePrefix"));

        // visibility of the card
        int numOfSeats = eventsPage.getSeatsCount(targetLocator);

        //booking tickets
        BookingFormPage bookingFormPage = eventsPage.proceedToBook(targetLocator);
        String bookingRef = bookingFormPage.fillAndConfirm(data.get("fullName"), data.get("email"), data.get("phone"));

        MyBookingsPage myBookingsPage = new MyBookingsPage(page);
        myBookingsPage.viewMyBookings();
        myBookingsPage.checkingBookedEvent(bookingRef);

        //checking seats
        myBookingsPage.goToEventsPage();
        int numOfSeatsAfterBooking = myBookingsPage.checkingSeatsAfterBooking(data.get("titlePrefix"));
        Assert.assertTrue(numOfSeats>numOfSeatsAfterBooking);

        //page.waitForTimeout(3000);

    }

}
