package boysband.selenium.support

import org.junit.jupiter.api.AfterEach
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import java.util.stream.Stream


abstract class BaseKinopoiskTest {

    protected lateinit var driver: WebDriver
    protected lateinit var wait: WebDriverWait

    
    protected fun open(browser: Browser, url: String = Config.baseUrl) {
        driver = DriverFactory.create(browser)
        driver.manage().timeouts().implicitlyWait(Config.implicitWait)
        wait = WebDriverWait(driver, Config.explicitWait)
        driver.get(url)
        acceptCookiesIfPresent()
    }

    @AfterEach
    fun tearDown() {
        if (::driver.isInitialized) driver.quit()
    }



    
    protected fun waitVisible(xpath: String): WebElement =
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)))

    
    protected fun waitClickable(xpath: String): WebElement =
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)))

    
    protected fun exists(xpath: String): Boolean = driver.findElements(By.xpath(xpath)).isNotEmpty()

    
    protected fun scrollIntoView(element: WebElement) {
        (driver as JavascriptExecutor)
            .executeScript("arguments[0].scrollIntoView({block: 'center'});", element)
    }

    
    protected fun scrollToBottom(steps: Int = 4) {
        val js = driver as JavascriptExecutor
        repeat(steps) {
            js.executeScript("window.scrollBy(0, document.body.scrollHeight / arguments[0]);", steps)
            Thread.sleep(400)
        }
    }

    
    private fun acceptCookiesIfPresent() {
        val consentButton =
            "//button[contains(., 'Принять') or contains(., 'Хорошо') or contains(., 'Соглас')]"
        try {
            val shortWait = WebDriverWait(driver, Duration.ofSeconds(1))
            shortWait.until(ExpectedConditions.elementToBeClickable(By.xpath(consentButton))).click()
        } catch (_: org.openqa.selenium.TimeoutException) {

        }
    }

    companion object {
        
        @JvmStatic
        fun browsers(): Stream<Browser> = Config.browsers.stream()
    }
}
