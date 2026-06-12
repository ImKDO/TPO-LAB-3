package boysband.selenium.pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait


class ReviewsPage(private val driver: WebDriver, private val wait: WebDriverWait) {

    private val reviewItem =
        "//div[contains(@class,'reviewItem') or contains(@class,'response') or @itemprop='review']"
    private val reviewText =
        "//*[contains(@class,'brand_words') or contains(@class,'_reachbanner_') or @itemprop='reviewBody']"

    fun waitLoaded() {
        wait.until(
            ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.xpath(reviewItem)),
                ExpectedConditions.presenceOfElementLocated(By.xpath(reviewText)),
            ),
        )
    }

    fun hasReviews(): Boolean =
        driver.findElements(By.xpath(reviewItem)).isNotEmpty() ||
            driver.findElements(By.xpath(reviewText)).isNotEmpty()

    fun reviewCount(): Int = driver.findElements(By.xpath(reviewItem)).size
}
