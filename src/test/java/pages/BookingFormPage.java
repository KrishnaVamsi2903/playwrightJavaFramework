package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingFormPage {

    Page page;
    private static final String nameLabel = "Full Name";
    private static final String emailLocator = "#customer-email";
    private static final String phoneLocator = "#phone";
    private static final String bookingRefLocator = ".booking-ref";

    public BookingFormPage(Page page){
        this.page = page;
    }

    public String fillAndConfirm(String name, String email, String phoneNumber){
        page.getByLabel(nameLabel).fill(name);
        page.locator(emailLocator).fill(email);
        page.locator(phoneLocator).fill(phoneNumber);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm Booking")).click();
        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
        String bookingRef = page.locator(bookingRefLocator).innerText();
        return bookingRef;
    }
}
