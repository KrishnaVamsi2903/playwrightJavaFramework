package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

public class MoreUIValidationsTest {

    Playwright playwright;
    Browser browser;
    Page page;
    BrowserContext context;

    @BeforeMethod (alwaysRun = true)
    public void SetUp(){
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
                .setPath(Paths.get("trace.zip"))
        );

    }

    @Test (description = "Child Window Handling")
    public void ChildWindowHandle(){
        Locator blinkingTexts = page.locator(".blinkingText");
        Page newPage = context.waitForPage(()->blinkingTexts.first().click());
        newPage.waitForLoadState();
        String loginID = newPage.locator("strong a").innerText();
        page.getByLabel("Username:").fill(loginID);
        System.out.println(page.getByLabel("Username:").inputValue());

        //page.waitForTimeout(3000);
    }

    @Test(groups = {"smoke"})
    public void UIControls(){
        Locator userRadio = page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName(" User"));
        userRadio.click();
        Assert.assertTrue(userRadio.isChecked());

        page.waitForTimeout(1000);
        page.locator("#okayBtn").click();

        page.getByRole(AriaRole.COMBOBOX).selectOption("Teacher");

        Locator termsCheckBox = page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("I Agree to the terms and conditions"));
        termsCheckBox.check();
        Assert.assertTrue(termsCheckBox.isChecked());

        //page.waitForTimeout(5000);
    }
}
