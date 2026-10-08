package pages2;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class LoginPage {
    Page page;
    String base_url;

    private static final String emailPlaceholder = "you@email.com";
    private static final String passwordLabel = "Password";

    public LoginPage(Page page, String base_url){
        this.page = page;
        this.base_url = base_url;

    }

    public DashboardPage loginToApplication(){

        page.navigate(base_url);
        System.out.println(page.title());
        page.getByPlaceholder(emailPlaceholder).fill("krishnavamsiseereddy@gmail.com");
        page.getByLabel(passwordLabel).fill("Krishna@123");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        DashboardPage dashboardPage = new DashboardPage(page);
        return dashboardPage;

    }

}
