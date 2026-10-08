package tests2;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MoreUIValidations2Test {

    Playwright playwright;
    Browser browser;
    Page page;
    BrowserContext context;

    @BeforeMethod
    public void setup(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();

        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true)
        );

        page = context.newPage();
        page.navigate("https://rahulshettyacademy.com/loginpagePractise/");
    }

    @AfterMethod
    public void TearDown(){
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace2.zip"))
        );
    }

    @Test
    public void ChildWindowHandleTest(){
        Locator blinkingTexts = page.locator(".blinkingText");
        Page newPage = context.waitForPage(()->blinkingTexts.first().click());
        newPage.waitForLoadState();
        String id = newPage.locator("strong a").innerText();
        page.getByLabel("Username:").fill(id);
        page.waitForTimeout(3000);
    }

    @Test
    public void UIControls(){

        //Radio button click
        Locator radioButton = page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName(" User"));
        radioButton.check();
        assertThat(radioButton).isChecked();
        page.locator(".btn-success").click();

        //select dropdown handling
        page.locator("div select").selectOption("Teacher");

        //handling checkbox
        page.getByText(" I Agree to the terms and conditions").check();


    }
}
