package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MockWebTest {

        Playwright playwright;
        Browser browser;
        Page page;

        @BeforeMethod
        public void SetUp(){
            playwright = Playwright.create();
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            page = browser.newPage();
            page.setDefaultTimeout(7000);
            page.navigate("https://eventhub.rahulshettyacademy.com/login");
        }

        @Test(description = "Sandbox banner is shown when 6 events are returned")
        public void DemoTest() {

            page.getByPlaceholder("you@email.com").fill("krishnavamsiseereddy@gmail.com");
            page.getByLabel("Password").fill("Krishna@123");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
            assertThat(
                    page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))
            ).isVisible();

            //step 1 - Create event from admin page
            page.route("**/api/events**", route -> route.fulfill(
                    new Route.FulfillOptions().setPath(Paths.get("src/test/resources/events.json"))
            ));

            page.navigate("https://eventhub.rahulshettyacademy.com/events");


            Locator eventCards = page.getByTestId("event-card");
            assertThat(eventCards.first()).isVisible();
            Assert.assertEquals(eventCards.count(), 2);
            assertThat(page.locator(".mx-1").first()).isHidden();

            page.route("**/api/events**", route -> route.fulfill(
                    new Route.FulfillOptions().setPath(Paths.get("src/test/resources/events6.json"))
            ));

            page.navigate("https://eventhub.rahulshettyacademy.com/events");

            Locator eventCards1 = page.getByTestId("event-card");
            assertThat(eventCards1.first()).isVisible();
            Assert.assertEquals(eventCards1.count(), 6);
            assertThat(page.locator(".mx-1").first()).isVisible();
        }

        @Test
        public void RouteResumeTest() {

            page.getByPlaceholder("you@email.com").fill("krishnavamsiseereddy@gmail.com");
            page.getByLabel("Password").fill("Krishna@123");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();


            //route.resume()

            page.getByTestId("nav-bookings").click();
            page.route("**/api/bookings/**", route -> route.resume(
                    new Route.ResumeOptions().setUrl("https://api.eventhub.rahulshettyacademy.com/api/bookings/20050"))
            );

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View Details")).first().click();
            assertThat(page.getByText("Booking not found")).isVisible();

            page.waitForTimeout(5000);

        }

}
