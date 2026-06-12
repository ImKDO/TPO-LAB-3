package boysband.selenium.pages

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait


class NamePage(private val driver: WebDriver, private val wait: WebDriverWait) {

    private val nameXpath =
        "//h1[contains(@class,'styles_primaryName') or @itemprop='name' or @data-tid]"

    private val filmographyRow =
        "//body//a[contains(@href,'/film/') or contains(@href,'/series/')] " +
            "| //div[contains(@class,'filmography') or contains(@class,'Filmography')]//a " +
            "| //section[contains(@class,'filmography')]//a"

    fun waitLoaded() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(nameXpath)))
    }

    fun name(): String = driver.findElement(By.xpath(nameXpath)).text

    fun hasFilmography(): Boolean {
        val js = driver as JavascriptExecutor
        repeat(5) {
            if (driver.findElements(By.xpath(filmographyRow)).isNotEmpty()) return true
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);")
            Thread.sleep(500)
        }
        return driver.findElements(By.xpath(filmographyRow)).isNotEmpty()
    }

    fun filmographyCount(): Int = driver.findElements(By.xpath(filmographyRow)).size
}
