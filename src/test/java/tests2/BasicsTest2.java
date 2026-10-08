package tests2;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasicsTest2 {

    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeMethod
    public void SetUp(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        page.navigate("https://eventhub.rahulshettyacademy.com/login");
        System.out.println(page.title());
    }

    @Test
    public void DemoTest(){

        // Login part
        page.getByPlaceholder("you@email.com").fill("krishnavamsiseereddy@gmail.com");
        page.getByLabel("Password").fill("Krishna@123");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

        String CName = "AR Rahman Singing Concert";
        //Creating an Event
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Admin")).click();
        page.getByRole(AriaRole.CONTENTINFO).getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Manage Events")).click();
        page.getByTestId("event-title-input").fill(CName);
        page.locator("div textarea").fill("Music concert with Tamil and Hindi songs");
        page.locator("#category").selectOption("Concert");
        page.getByLabel("City").fill("Bangalore");
        page.getByPlaceholder("Venue name & address").fill("KTPO Ground, EPIP Zone, Whitefield");
        page.getByLabel("Event Date & Time").fill("2026-10-03T18:00");
        page.getByLabel("Price ($)").fill("3000");
        page.locator("#total-seats").fill("100000");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Event")).click();

        //checking event created or not
        page.locator("#nav-events").click();
        Locator eventCards = page.locator("#event-card");
        Locator target = eventCards.filter(new Locator.FilterOptions().setHasText(CName));
        page.waitForTimeout(2000);
        assertThat(target).isVisible();
        System.out.println(target.getByText("seats").innerText());
        int Count = Integer.parseInt(target.getByText("seats").innerText().split(" ")[0]);
        target.getByTestId("book-now-btn").click();

        //booking concert
        page.locator("#customerName").fill("Manikanta");
        page.getByLabel("Email").fill("mani@gmail.com");
        page.getByLabel("Phone Number").fill("9898989000");
        page.locator("#confirm-booking").click();

        //checking tickets booked or not
        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
        String BookingRef = page.locator(".booking-ref").innerText();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View My Bookings")).click();

        //validating the ticket
        Locator BookingCard = page.getByTestId("booking-card");
        assertThat(BookingCard.filter(new Locator.FilterOptions().setHasText(BookingRef))).isVisible();

        //checking event booking count
        page.locator("#nav-events").click();
        Locator eventCardsAfterBooking = page.locator("#event-card");
        Locator targetAfterBooking = eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText(CName));
        page.waitForTimeout(2000);
        System.out.println(targetAfterBooking.getByText("seats").innerText());
        int CountAfterBooking = Integer.parseInt(targetAfterBooking.getByText("seats").innerText().split(" ")[0]);
        Assert.assertTrue(CountAfterBooking < Count);





        page.waitForTimeout(3000);
    }



    }



