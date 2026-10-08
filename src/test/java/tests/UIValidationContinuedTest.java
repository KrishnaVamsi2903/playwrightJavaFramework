package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class UIValidationContinuedTest {

    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeMethod(alwaysRun = true)
    public void SetUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        page.navigate("https://rahulshettyacademy.com/AutomationPractice/");
    }

    @Test(groups = {"smoke"})
    public void DemoTest(){
        assertThat(page.getByPlaceholder("Hide/Show Example")).isVisible();
        page.locator("#hide-textbox").click();
        assertThat(page.getByPlaceholder("Hide/Show Example")).isHidden();
        page.onDialog(dialog -> dialog.accept());
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Alert")).click();

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Mouse Hover")).hover();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Top")).click();

        FrameLocator framePage = page.frameLocator("#courses-iframe");
        framePage.getByRole(AriaRole.LINK, new FrameLocator.GetByRoleOptions().setName("Learning paths")).click();
        String txtContent = framePage.locator(".inner-box h1").textContent();
        System.out.println(txtContent);



    }

    @Test
    public void screenShotTest(){
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("PageScreenshot.png")));
        Locator displayBox = page.getByPlaceholder("Hide/Show Example");
        displayBox.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get("displayBoxSS.png")));
        page.locator("#hide-textbox");
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("pagePostSS.png")));

    }
}
