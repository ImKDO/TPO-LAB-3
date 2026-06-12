package boysband.selenium.pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait


class SearchResultsPage(private val driver: WebDriver, private val wait: WebDriverWait) {

    private val filmResultLink = "//a[contains(@href,'/film/') and contains(@href,'/sr/')]"

    private val nameResultLink = "//a[contains(@href,'/name/') and contains(@href,'/sr/')]"

    private val anyResultItem =
        "//div[contains(@class,'element') or contains(@class,'search_results') " +
            "or contains(@class,'SearchPage')]//a[contains(@href,'/film/') or contains(@href,'/name/')]"

    private fun resultByText(text: String) =
        "//div[contains(@class,'element') or contains(@class,'search_results')]" +
            "//a[contains(., \"$text\")]"

    fun waitLoaded() {
        wait.until(
            ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.xpath(filmResultLink)),
                ExpectedConditions.presenceOfElementLocated(By.xpath(nameResultLink)),
                ExpectedConditions.presenceOfElementLocated(By.xpath(anyResultItem)),
            ),
        )
    }

    fun hasFilmResults(): Boolean = driver.findElements(By.xpath(filmResultLink)).isNotEmpty()

    fun hasNameResults(): Boolean = driver.findElements(By.xpath(nameResultLink)).isNotEmpty()

    fun openFirstFilm(): FilmPage {
        val link = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(filmResultLink)))
        driver.get(requireNotNull(link.getAttribute("href")) { "Result row must link to a film card" })
        return FilmPage(driver, wait)
    }

    fun openFirstName(): NamePage {
        val link = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(nameResultLink)))
        driver.get(requireNotNull(link.getAttribute("href")) { "Result row must link to a person card" })
        return NamePage(driver, wait)
    }

    private fun firstFilm(): WebElement =
        driver.findElement(By.xpath(filmResultLink))

    fun firstFilmTitle(): String = firstFilm().text
}
