package PHPTravel.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AccountVerificationPage {

    //////////////variables\\\\\\\\\\\\\\\\\\\
    WebDriver driver;
    String VerificationUrl = "https://phptravels.net/account/activation/";
    //////////////Locators\\\\\\\\\\\\\\\\\\\
    By AccountActivatedMsgLocator = By.xpath("//p//strong[contains(text(),'has been activated')]");

    public AccountVerificationPage(WebDriver driver) {
        this.driver = driver;
    }

    //////////////variables\\\\\\\\\\\\\\\\\\\

    public void verifyAccount(String UserId, String EmailCode) {
        String AccountVerificationUrl = VerificationUrl + UserId + "/" + EmailCode;
        driver.get(AccountVerificationUrl);
    }

    public String getAccountActivatedMsg() {
        WebElement VerifiedMsg = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(AccountActivatedMsgLocator));
        return VerifiedMsg.getText();
    }
}
