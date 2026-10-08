package tests2;

import com.microsoft.playwright.Locator;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages2.*;
import utils.DataProviderUtil;

import java.io.IOException;
import java.util.HashMap;

public class FrameworkBuildDataDrivenTest2 extends TestBase2 {

    @DataProvider(name = "eventBookingData")
    public Object[][] eventBookingData() throws IOException {
        return DataProviderUtil.getJsonDataToMap("/src/test/resources/eventBookingData.json");
    }

    @Test (dataProvider = "eventBookingData", description = "Create an Event - Book that event and verify if it's booked")
    public void DemoTest(HashMap<String, String> data){

        /*
        //navigation part
        page.navigate(base_url);
        System.out.println(page.title());

        // Login part
        page.getByPlaceholder("you@email.com").fill("krishnavamsiseereddy@gmail.com");
        page.getByLabel("Password").fill("Krishna@123");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        */

        //String CName = "AR Rahman Singing Concert";

        LoginPage loginPage = new LoginPage(page, base_url);
        DashboardPage dashboardPage = loginPage.loginToApplication();
        dashboardPage.waitForEventsToLoad();


        //Creating an Event
       /* page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Admin")).click();
        page.getByRole(AriaRole.CONTENTINFO).getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Manage Events")).click();
        page.getByTestId("event-title-input").fill(CName);
        page.locator("div textarea").fill("Music concert with Tamil and Hindi songs");
        page.locator("#category").selectOption("Concert");
        page.getByLabel("City").fill("Bangalore");
        page.getByPlaceholder("Venue name & address").fill("KTPO Ground, EPIP Zone, Whitefield");
        page.getByLabel("Event Date & Time").fill("2026-10-10T18:00");
        page.getByLabel("Price ($)").fill("3000");
        page.locator("#total-seats").fill("100000");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Event")).click(); */

        AdminEventsPage adminEventsPage = new AdminEventsPage(page);
        adminEventsPage.navigateToEventsPage();
        adminEventsPage.createEvent(data.get("titlePrefix"),data.get("description"), data.get("category"), data.get("city"),
                data.get("venue"), data.get("dateTime"), data.get("price"), data.get("totalSeats")
        );

        //checking event created or not
       /* page.locator("#nav-events").click();
        Locator eventCards = page.locator("#event-card");
        Locator target = eventCards.filter(new Locator.FilterOptions().setHasText(CName));
        assertThat(target).isVisible();
        System.out.println(target.getByText("seats").innerText());
        int Count = Integer.parseInt(target.getByText("seats").innerText().split(" ")[0]);
        target.getByTestId("book-now-btn").click(); */

        EventsPage eventsPage = new EventsPage(page);
        eventsPage.goTo();
        eventsPage.waitForEventsToLoad();
        Locator target = eventsPage.findEventCard(data.get("titlePrefix"));
        int Count = eventsPage.getSeatsCount(target);
        BookingFormPage bookingFormPage = eventsPage.proceedToBook(target);


        //booking concert
        /*
        page.locator("#customerName").fill("Manikanta");
        page.getByLabel("Email").fill("mani@gmail.com");
        page.getByLabel("Phone Number").fill("9898989000");
        page.locator("#confirm-booking").click();

        //checking tickets booked or not
        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
        String BookingRef = page.locator(".booking-ref").innerText();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View My Bookings")).click();

         */
        String BookingRef = bookingFormPage.fillAndConfirm(data.get("fullName"), data.get("email"), data.get("phone"));

        //validating the ticket
        /*
        Locator BookingCard = page.getByTestId("booking-card");
        assertThat(BookingCard.filter(new Locator.FilterOptions().setHasText(BookingRef))).isVisible();

        //checking event booking count
        page.locator("#nav-events").click();
        Locator eventCardsAfterBooking = page.locator("#event-card");
        Locator targetAfterBooking = eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText(CName));
        page.waitForTimeout(2000);
        System.out.println(targetAfterBooking.getByText("seats").innerText());
        int CountAfterBooking = Integer.parseInt(targetAfterBooking.getByText("seats").innerText().split(" ")[0]);

         */
        MyBookingsPage myBookingsPage = new MyBookingsPage(page);
        myBookingsPage.viewMyBookings();
        myBookingsPage.checkingBookedEvent(BookingRef);
        myBookingsPage.goToEventsPage();
        int CountAfterBooking = myBookingsPage.checkingSeatsAfterBooking(data.get("titlePrefix"));
        Assert.assertTrue(CountAfterBooking < Count);

        //page.waitForTimeout(3000);
    }



    }



