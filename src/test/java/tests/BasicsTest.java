package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasicsTest {

    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeMethod
    public void SetUp(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        page.navigate("https://eventhub.rahulshettyacademy.com/login");
        //PlaywrightAssertions.setDefaultAssertionTimeout(7000);
    }

    @Test(description = "Create an Event - Book that event and verify if it's booked")
    public void DemoTest(){

        System.out.println(page.title());
        assertThat(page).hasTitle("EventHub — Discover & Book Events");
        page.getByPlaceholder("you@email.com").fill("krishnavamsiseereddy@gmail.com");
        page.getByLabel("Password").fill("Krishna@123");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        assertThat(
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))
        ).isVisible();

        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");
        //Actions - Wait fo 10 Secs
        page.locator("#event-title-input").fill("KV QA Summit");
        page.locator("#admin-event-form div textarea").fill("We can discuss about AI powered Testing and it's effects.");
        page.getByLabel("Category").selectOption("Workshop");
        page.getByLabel("City").fill("Bangalore");
        page.getByPlaceholder("Venue name & address").fill("ABC Convention hall, Whitefields");
        page.getByLabel("Event Date & Time").fill("2026-10-17T04:00");
        page.getByLabel("Price ($)").fill("1000");
        page.getByLabel("Total Seats").fill("100");
        page.locator("#add-event-btn").click();
        assertThat(page.getByText("Event created!")).isVisible();

        //step-2
        page.locator("#nav-events").click();

        page.waitForTimeout(300);
        Locator eventCards = page.getByTestId("event-card");

        // visibility of the card

        Locator targetLocator = eventCards.filter(new Locator.FilterOptions().setHasText("KV QA Summit"));
        String seatsText = targetLocator.getByText("seats").innerText();
        int numOfSeats = Integer.parseInt(seatsText.split(" ")[0]);
        System.out.println(numOfSeats);

        //booking tickets
        targetLocator.getByTestId("book-now-btn").click();
        //assertThat(targetLocator).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000)); // assertion waits for 5 sec
        //page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+")).click();
        //assertThat(page.locator("#ticket-count")).hasText("2");
        page.getByLabel("Full Name").fill("Prasantha");
        page.locator("#customer-email").fill("pransantha@gmail.com");
        page.locator("#phone").fill("9876543210");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm Booking")).click();
        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
        String bookingRef = page.locator(".booking-ref").innerText();

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View My Bookings")).click();
        /*String bookingRef2 = page.locator(".booking-ref").first().innerText();
        if(bookingRef.equals(bookingRef2)){
            System.out.println("Booked");
        }*/

        Locator bookingCard = page.locator("#booking-card");
        Locator bookingRef2 = bookingCard.filter(new Locator.FilterOptions().setHasText(bookingRef));
        assertThat(bookingRef2).isVisible();

        //checking seats

        page.locator("#nav-events").click();

        page.waitForTimeout(2000);
        Locator eventCardsAfterBooking = page.getByTestId("event-card");
        Locator targetLocatorAfterBooking = eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText("KV QA Summit"));
        String seatsTextAfterBooking = targetLocatorAfterBooking.getByText("seats").innerText();
        int numOfSeatsAfterBooking = Integer.parseInt(seatsTextAfterBooking.split(" ")[0]);
        System.out.println(numOfSeatsAfterBooking);
        Assert.assertTrue(numOfSeats>numOfSeatsAfterBooking);


        //page.waitForTimeout(3000);







    }

}
