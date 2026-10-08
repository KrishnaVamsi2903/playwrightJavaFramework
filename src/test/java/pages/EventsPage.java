package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class EventsPage {

    Page page;
    public EventsPage(Page page){
        this.page = page;
    }

    public void goTo(){
        page.locator("#nav-events").click();
    }

    public Locator waitForEventsToLoad(){
        Locator eventCards = page.getByTestId("event-card");
        assertThat(eventCards.first()).isVisible();
        return eventCards;
    }

    public Locator findEventCard(String titleCard){
        Locator eventCards = waitForEventsToLoad();
        Locator targetLocator = eventCards.filter(new Locator.FilterOptions().setHasText(titleCard));

        assertThat(targetLocator).isVisible();
        return targetLocator;
    }

    public int getSeatsCount(Locator targetLocator){
        //Locator targetLocator = findEventCard(titleCard);
        String seatsText = targetLocator.getByText("seats").innerText();
        int numOfSeats = Integer.parseInt(seatsText.split(" ")[0]);
        System.out.println(numOfSeats);
        return numOfSeats;
    }

    public BookingFormPage proceedToBook(Locator targetLocator){
        //Locator targetLocator = findEventCard(titleCard);
        targetLocator.getByTestId("book-now-btn").click();
        return new BookingFormPage(page);
    }
}
