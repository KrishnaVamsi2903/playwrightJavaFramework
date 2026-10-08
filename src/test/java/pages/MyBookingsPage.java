package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MyBookingsPage {
    Page page;
    private static final String bookingCardLocator = "#booking-card";
    private static final String eventsLocator = "#nav-events";
    private static final String eventCardTestId = "event-card";

    public MyBookingsPage(Page page){
        this.page = page;
    }

    public void viewMyBookings(){
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View My Bookings")).click();
    }

    public void checkingBookedEvent(String bookingRef){
        Locator bookingCard = page.locator(bookingCardLocator);
        Locator bookingRef2 = bookingCard.filter(new Locator.FilterOptions().setHasText(bookingRef));
        assertThat(bookingRef2).isVisible();
    }

    public int checkingSeatsAfterBooking(String eventTitle){
        page.locator(eventsLocator).click();

        assertThat(page.getByTestId(eventCardTestId).first()).isVisible();
        Locator eventCardsAfterBooking = page.getByTestId(eventCardTestId);
        Locator targetLocatorAfterBooking = eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText(eventTitle));
        page.waitForTimeout(1000);
        String seatsTextAfterBooking = targetLocatorAfterBooking.getByText("seats").innerText();
        int numOfSeatsAfterBooking = Integer.parseInt(seatsTextAfterBooking.split(" ")[0]);

        System.out.println(numOfSeatsAfterBooking);
        return numOfSeatsAfterBooking;
    }

    public void goToEventsPage(){
        page.locator(eventsLocator).click();
    }
}
