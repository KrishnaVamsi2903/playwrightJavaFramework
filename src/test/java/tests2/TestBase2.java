package tests2;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TestBase2 {


    Playwright playwright;
    Browser browser;
    Page page;
    String base_url;

    @BeforeMethod
    public void SetUp() throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
        prop.load(fis);
        String browserName = prop.getProperty("browser");
        playwright = Playwright.create();

        if ("firefox".equals(browserName)){
            playwright.firefox().launch();
        } else if ("safari".equals(browserName)) {
            playwright.webkit().launch();
        }
        else {
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        }

        page = browser.newPage();
        base_url=prop.getProperty("qa.baseurl");

    }
}
