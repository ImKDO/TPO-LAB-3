package boysband.selenium.pages

import boysband.selenium.support.Config
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.net.URLEncoder


class MainPage(private val driver: WebDriver, private val wait: WebDriverWait) {

    private val searchInput = "//input[@name='kp_query' or @type='search' or @placeholder]"

    
    fun search(query: String): SearchResultsPage {
        val input = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(searchInput)))
        input.clear()
        input.sendKeys(query)
        driver.get("${Config.baseUrl}/index.php?kp_query=" + URLEncoder.encode(query, Charsets.UTF_8))
        return SearchResultsPage(driver, wait)
    }

    
    fun isLoaded(): Boolean =
        driver.findElements(By.xpath(searchInput)).isNotEmpty()
}
