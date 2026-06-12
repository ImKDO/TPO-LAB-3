package boysband.selenium.pages

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait


class CatalogPage(private val driver: WebDriver, private val wait: WebDriverWait) {

    private val anyFilmCard =
        "//a[contains(@href,'/film/') or contains(@href,'/series/')]"

    private val genreFilterButton =
        "//a[contains(@href,'genre')] " +
            "| //button[contains(., 'Жанр') or contains(@data-id,'genre') or contains(@class,'genre')] " +
            "| //select[contains(@name,'genre') or contains(@id,'genre')] " +
            "| //*[@data-filter-type='genre' or @data-type='genre']"

    private val yearFilterButton =
        "//a[contains(@href,'year')] " +
            "| //button[contains(., 'Год') or contains(@data-id,'year') or contains(@class,'year')] " +
            "| //select[contains(@name,'year') or contains(@id,'year')] " +
            "| //input[@type='number' or contains(@placeholder,'год') or contains(@placeholder,'Год')]"

    private val countryFilterButton =
        "//a[contains(@href,'country')] " +
            "| //button[contains(., 'Стран') or contains(@data-id,'country') or contains(@class,'country')] " +
            "| //select[contains(@name,'country') or contains(@id,'country')] " +
            "| //*[@data-filter-type='country' or @data-type='country']"

    private val recommendationsHeading =
        "//*[contains(translate(.,'ПОХОЖИЕ','похожие'),'похожие') " +
            "or contains(translate(.,'РЕКОМЕНДАЦИИ','рекомендации'),'рекомендации') " +
            "or contains(@class,'similar') or contains(@class,'Similar') " +
            "or contains(@class,'recommend') or contains(@class,'Recommend')]"

    fun waitLoaded() {
        wait.until(
            ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.xpath(anyFilmCard)),
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//h1 | //h2 | //*[contains(@class,'CatalogPage') or contains(@class,'catalog')]"),
                ),
            ),
        )
    }

    fun resultCount(): Int = driver.findElements(By.xpath(anyFilmCard)).size

    fun hasGenreFilter(): Boolean = driver.findElements(By.xpath(genreFilterButton)).isNotEmpty()

    fun hasYearFilter(): Boolean = driver.findElements(By.xpath(yearFilterButton)).isNotEmpty()

    fun hasCountryFilter(): Boolean = driver.findElements(By.xpath(countryFilterButton)).isNotEmpty()

    fun selectGenre(genre: String) {
        clickFirst(genreFilterButton)
        val option = "//*[contains(., '$genre') and (contains(@class,'option') or @role='option')]"
        clickFirst(option)
    }

    fun hasRecommendations(currentFilmId: String): Boolean {
        val otherFilmLink =
            "//body//a[contains(@href,'/film/') and not(contains(@href,'$currentFilmId'))]"
        val js = driver as JavascriptExecutor
        repeat(5) {
            if (driver.findElements(By.xpath(recommendationsHeading)).isNotEmpty() ||
                driver.findElements(By.xpath(otherFilmLink)).isNotEmpty()
            ) {
                return true
            }
            js.executeScript("window.scrollBy(0, window.innerHeight * 2);")
            Thread.sleep(400)
        }
        return false
    }

    private fun clickFirst(xpath: String) {
        val element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)))
        (driver as JavascriptExecutor).executeScript("arguments[0].scrollIntoView({block:'center'});", element)
        element.click()
    }
}
