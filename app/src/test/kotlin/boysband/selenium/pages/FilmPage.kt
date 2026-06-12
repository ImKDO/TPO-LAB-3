package boysband.selenium.pages

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait


class FilmPage(private val driver: WebDriver, private val wait: WebDriverWait) {

    private val titleXpath =
        "//h1[contains(@class,'styles_title') or @itemprop='name' or @data-tid]"
    private val ratingXpath =
        "//*[contains(@class,'film-rating') or contains(@class,'styles_rating') " +
            "or @itemprop='ratingValue' or contains(@class,'Rating')]"
    private val synopsisXpath =
        "//*[contains(@class,'styles_synopsis') or @itemprop='description' " +
            "or contains(@class,'synopsis') or contains(@class,'Summary')]"
    private val castXpath =
        "//div[contains(@class,'styles_actors') or contains(@class,'cast') or @itemprop='actor']" +
            "//a[contains(@href,'/name/')]" +
            " | //a[@itemprop='actor' or contains(@class,'actorItem')]"
    private val castMemberLink =
        "//div[contains(@class,'styles_actors') or contains(@class,'cast') " +
            "or contains(@class,'film-crew')]//a[contains(@href,'/name/')]" +
            " | //a[@itemprop='actor']"
    private val trailerButton =
        "//button[contains(translate(.,'ТРЕЙЛЕР','трейлер'),'трейлер')] " +
            "| //a[contains(translate(.,'ТРЕЙЛЕР','трейлер'),'трейлер')]"
    private val trailerPlayer =
        "//iframe[contains(@src,'video') or contains(@src,'kinopoisk') or contains(@src,'youtube')] " +
            "| //video"
    private val reviewsLink =
        "//a[contains(@href,'/reviews/') or contains(translate(.,'РЕЦЕНЗИИ','рецензии'),'рецензии')]"
    private val watchButton =
        "//a[contains(@href,'/watch') or contains(translate(.,'СМОТРЕТЬ','смотреть'),'смотреть')] " +
            "| //button[contains(translate(.,'СМОТРЕТЬ','смотреть'),'смотреть')]"
    private val rateControl =
        // Star rating is usually input[type='radio'] — clicking any star triggers auth
        "//input[@type='radio'] " +
            // Fallback: explicit text in button/link/leaf element
            "| //button[normalize-space(text())='Оценить'] " +
            "| //a[normalize-space(text())='Оценить'] " +
            "| //*[normalize-space(text())='Оценить'][@role='button' or @tabindex or self::button or self::a] " +
            "| //*[@aria-label='Оценить' or @title='Оценить'] " +
            "| //*[contains(@data-tid,'rate') or contains(@data-tid,'vote') " +
            "or contains(@data-testid,'rate') or contains(@data-testid,'vote')]"
    private val willWatchButton =
        "//button[contains(., 'Буду смотреть') and not(.//button)] " +
            "| //a[contains(., 'Буду смотреть') and not(.//a)] " +
            "| //span[contains(., 'Буду смотреть') and not(*)][@role='button' or @tabindex] " +
            "| //div[contains(., 'Буду смотреть') and not(*)][@role='button' or @tabindex]"
    private val writeReviewButton =
        "//button[contains(translate(.,'НАПИСАТЬ','написать'),'написать рецензию')] " +
            "| //a[contains(translate(.,'НАПИСАТЬ','написать'),'написать рецензию')]"

    fun waitLoaded() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(titleXpath)))
    }

    fun title(): String = driver.findElement(By.xpath(titleXpath)).text

    fun hasRating(): Boolean = driver.findElements(By.xpath(ratingXpath)).isNotEmpty()

    fun hasSynopsis(): Boolean = driver.findElements(By.xpath(synopsisXpath)).isNotEmpty()

    fun hasCast(): Boolean = driver.findElements(By.xpath(castXpath)).isNotEmpty()

    fun openFirstCastMember(): NamePage {
        val member = driver.findElement(By.xpath(castMemberLink))
        (driver as JavascriptExecutor).executeScript("arguments[0].scrollIntoView({block:'center'});", member)
        wait.until(ExpectedConditions.elementToBeClickable(member)).click()
        return NamePage(driver, wait)
    }

    fun hasTrailer(): Boolean = driver.findElements(By.xpath(trailerButton)).isNotEmpty()

    fun playTrailer(): Boolean {
        val button = driver.findElements(By.xpath(trailerButton)).firstOrNull() ?: return false
        (driver as JavascriptExecutor).executeScript("arguments[0].scrollIntoView({block:'center'});", button)
        wait.until(ExpectedConditions.elementToBeClickable(button)).click()
        return wait.until(
            ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.xpath(trailerPlayer)),
                ExpectedConditions.urlContains("/video/"),
            ),
        )
    }

    fun hasReviewsLink(): Boolean = driver.findElements(By.xpath(reviewsLink)).isNotEmpty()

    fun openReviews(): ReviewsPage {
        val link = driver.findElement(By.xpath(reviewsLink))
        (driver as JavascriptExecutor).executeScript("arguments[0].scrollIntoView({block:'center'});", link)
        wait.until(ExpectedConditions.elementToBeClickable(link)).click()
        return ReviewsPage(driver, wait)
    }

    fun hasWatchButton(): Boolean = driver.findElements(By.xpath(watchButton)).isNotEmpty()

    fun clickRate() = clickFirst(rateControl)

    fun clickWillWatch() = clickFirst(willWatchButton)

    fun clickWriteReview() = clickFirst(writeReviewButton)

    private fun clickFirst(xpath: String) {
        val element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)))
        (driver as JavascriptExecutor).executeScript("arguments[0].scrollIntoView({block:'center'});", element)
        try {
            element.click()
        } catch (_: org.openqa.selenium.ElementClickInterceptedException) {
            (driver as JavascriptExecutor).executeScript("arguments[0].click();", element)
        }
    }
}
