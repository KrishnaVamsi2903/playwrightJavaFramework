package pages2;

import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingFormPage {

    Page page;
    private static final String nameLocator = "#customerName";
    private static final String emailLabel = "Email";
    private static final String phoneLabel = "Phone Number";
    private static final String bookingRefLocator = ".booking-ref";
    private static final String confirmBookingLocator = "#confirm-booking";

    public BookingFormPage(Page page) {
        this.page = page;
    }

    public String fillAndConfirm(String name, String email, String phone){
        page.locator(nameLocator).fill(name);
        page.getByLabel(emailLabel).fill(email);
        page.getByLabel(phoneLabel).fill(phone);
        page.locator(confirmBookingLocator).click();

        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
        String bookingRef = page.locator(bookingRefLocator).innerText();
        return bookingRef;
    }
}
