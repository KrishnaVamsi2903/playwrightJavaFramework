package tests2;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

public class UIValidationContinuedTest2 {

    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeMethod
    public void SetUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        page.navigate("https://rahulshettyacademy.com/AutomationPractice/");
    }

    @Test
    public void DemoTest(){

        page.onDialog(dialog -> dialog.accept());
        page.locator("#alertbtn").click();

        page.locator("#mousehover").hover();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Top")).click();

        FrameLocator frameLocator = page.frameLocator("#courses-iframe");
        frameLocator.getByRole(AriaRole.LINK, new FrameLocator.GetByRoleOptions().setName("Learning paths")).click();
        String text = frameLocator.locator("div h1").innerText();
        System.out.println(text);

    }

    @Test
    public void ScreenShotTest(){
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("ScreenShot1.png")));
        Locator displayBox = page.getByPlaceholder("Hide/Show Example");
        displayBox.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get("dialogImg.png")));
        page.locator("#hide-textbox");
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("PostSS.png")));
    }
}
