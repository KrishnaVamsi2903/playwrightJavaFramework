package pages;

import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AdminEventsPage {

    Page page;
    private static final String eventTitle = "#event-title-input";
    private static final String textArea = "#admin-event-form div textarea";
    private static final String categoryLabel = "Category";
    private static final String cityLabel = "City";
    private static final String addressPlaceholder = "Venue name & address";
    private static final String timeAndDate = "Event Date & Time";
    private static final String priceLabel = "Price ($)";
    private static final String totalSeatsLabel = "Total Seats";
    private static final String addEventButton = "#add-event-btn";
    private static final String successToast = "Event created!";

    public AdminEventsPage(Page page){
        this.page = page;
    }

    public void navigateToEventsPage(){
        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");
    }

    public void createEvent(String title, String description, String category, String city, String address,
                            String time, String price, String totalseats){
        page.locator(eventTitle).fill(title);
        page.locator(textArea).fill(description);
        page.getByLabel(categoryLabel).selectOption(category);
        page.getByLabel(cityLabel).fill(city);
        page.getByPlaceholder(addressPlaceholder).fill(address);
        page.getByLabel(timeAndDate).fill(time);
        page.getByLabel(priceLabel).fill(price);
        page.getByLabel(totalSeatsLabel).fill(totalseats);
        page.locator(addEventButton).click();
        assertThat(page.getByText(successToast)).isVisible();

    }
}
