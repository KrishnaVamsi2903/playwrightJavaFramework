package pages2;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AdminEventsPage {
    Page page;
    private static final String eventTitleTestId = "event-title-input";
    private static final String textareaLocator = "div textarea";
    private static final String categoryLocator = "#category";
    private static final String cityLocator = "City";
    private static final String venuePlaceholder = "Venue name & address";
    private static final String timeDateLabel = "Event Date & Time";
    private static final String priceLabel = "Price ($)";
    private static final String totalSeatsLabel = "#total-seats";
    private static final String successToast = "Event created!";


    public AdminEventsPage(Page page){
        this.page = page;
    }

    public void navigateToEventsPage(){
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Admin")).click();
        page.getByRole(AriaRole.CONTENTINFO).getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Manage Events")).click();
    }

    public void createEvent(String title, String description, String category, String city, String venue,
                            String timeDate, String price, String totalSeats){
        page.getByTestId(eventTitleTestId).fill(title);
        page.locator(textareaLocator).fill(description);
        page.locator(categoryLocator).selectOption(category);
        page.getByLabel(cityLocator).fill(city);
        page.getByPlaceholder(venuePlaceholder).fill(venue);
        page.getByLabel(timeDateLabel).fill(timeDate);
        page.getByLabel(priceLabel).fill(price);
        page.locator(totalSeatsLabel).fill(totalSeats);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Event")).click();
        assertThat(page.getByText(successToast)).isVisible();
    }

}
