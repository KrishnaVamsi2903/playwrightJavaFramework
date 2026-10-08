package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FrameworkBuildTest extends TestBase{

    @Test(groups = {"framework"}, description = "Create an Event - Book that event and verify if it's booked")
    public void DemoTest(){

        String eventTitle = "KV QA Summit";
        LoginPage loginPage = new LoginPage(page, base_url);
        DashboardPage dashboardPage = loginPage.loginToApplication();
        dashboardPage.waitForEventsToLoad();
        AdminEventsPage adminEventsPage = new AdminEventsPage(page);
        adminEventsPage.navigateToEventsPage();
        adminEventsPage.createEvent(eventTitle, "We can discuss about AI powered Testing and it's effects.", "Workshop", "Bangalore",
                "ABC Convention hall, Whitefields", "2026-10-17T04:00", "1000", "100"
        );

        //step-2
        EventsPage eventsPage = new EventsPage(page);
        eventsPage.goTo();
        Locator targetLocator = eventsPage.findEventCard(eventTitle);

        // visibility of the card
        int numOfSeats = eventsPage.getSeatsCount(targetLocator);

        //booking tickets
        BookingFormPage bookingFormPage = eventsPage.proceedToBook(targetLocator);
        String bookingRef = bookingFormPage.fillAndConfirm("Prasantha", "pransantha@gmail.com", "9876543210");

        MyBookingsPage myBookingsPage = new MyBookingsPage(page);
        myBookingsPage.viewMyBookings();
        myBookingsPage.checkingBookedEvent(bookingRef);

        //checking seats
        myBookingsPage.goToEventsPage();
        int numOfSeatsAfterBooking = myBookingsPage.checkingSeatsAfterBooking(eventTitle);
        Assert.assertTrue(numOfSeats>numOfSeatsAfterBooking);

        //page.waitForTimeout(3000);

    }

}
