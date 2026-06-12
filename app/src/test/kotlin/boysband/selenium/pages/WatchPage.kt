package boysband.selenium.pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait


class WatchPage(private val driver: WebDriver, private val wait: WebDriverWait) {

    private val playerShell =
        "//div[contains(@class,'player') or contains(@class,'kinopoisk-watch') " +
            "or contains(@class,'watch') or contains(@class,'Player')] " +
            "| //video | //iframe[contains(@src,'player') or contains(@src,'watch')]"

    private val purchaseOrSubscribe =
        "//button[contains(., 'Смотреть') or contains(., 'Подписк') or contains(., 'Купить') " +
            "or contains(., 'оформить')] " +
            "| //a[contains(., 'Смотреть') or contains(., 'Подписк') or contains(., 'Купить')]"

    private val tvChannelTile =
        "//a[contains(@href,'channel') or contains(@href,'tv') or contains(@href,'kanal')] " +
            "| //*[contains(@class,'channel') or contains(@class,'Channel') or contains(@class,'tv-tile')] " +
            "| //*[contains(., 'канал') or contains(., 'Канал') or contains(., 'ТВ') or contains(., 'эфир')]" +
            "[self::a or self::div or self::article]"

    fun waitPlayerLoaded() {
        wait.until(
            ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.xpath(playerShell)),
                ExpectedConditions.presenceOfElementLocated(By.xpath(purchaseOrSubscribe)),
            ),
        )
    }

    fun hasPlayerOrPurchaseGate(): Boolean =
        driver.findElements(By.xpath(playerShell)).isNotEmpty() ||
            driver.findElements(By.xpath(purchaseOrSubscribe)).isNotEmpty()

    fun waitTvLoaded() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(tvChannelTile)))
    }

    fun tvChannelCount(): Int = driver.findElements(By.xpath(tvChannelTile)).size
}
