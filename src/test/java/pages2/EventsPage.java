package pages2;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class EventsPage {

    Page page;
    private static final String eventsLocator = "#nav-events";
    private static final String eventCardLocator = "#event-card";

    public EventsPage(Page page){
        this.page = page;
    }

    public void goTo(){
        page.locator(eventsLocator).click();
    }

    public Locator waitForEventsToLoad(){
        Locator eventCards = page.locator(eventCardLocator);
        assertThat(eventCards.first()).isVisible();
        return eventCards;
    }

    public Locator findEventCard(String title){
        Locator eventCards = waitForEventsToLoad();
        Locator target = eventCards.filter(new Locator.FilterOptions().setHasText(title));
        //System.out.println(target);
        assertThat(target).isVisible();
        return target;
    }

    public int getSeatsCount(Locator target){
        System.out.println(target.getByText("seats").innerText());
        int count = Integer.parseInt(target.getByText("seats").innerText().split(" ")[0]);
        return count;
    }

    public BookingFormPage proceedToBook(Locator target){
        target.getByTestId("book-now-btn").click();
        return new BookingFormPage(page);
    }




}
