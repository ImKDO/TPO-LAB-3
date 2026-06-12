package boysband.selenium.pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait


class AuthGate(private val driver: WebDriver, private val wait: WebDriverWait) {

    private val loginModal =
        // Input fields that only appear inside a login form
        "//input[@type='tel'] " +
            "| //input[@type='email'][not(@name='kp_query')] " +
            "| //input[contains(@autocomplete,'tel') or contains(@autocomplete,'username') " +
            "or contains(@name,'login') or contains(@name,'phone') or contains(@name,'account')] " +
            // ARIA/HTML dialog
            "| //*[@aria-modal='true'] " +
            "| //*[@aria-labelledby][contains(@role,'dialog')] " +
            // OAuth links injected inside the modal (absent on plain page)
            "| //a[contains(@href,'passport.yandex') or contains(@href,'oauth.yandex') " +
            "or contains(@href,'id.kinopoisk') or contains(@href,'login?retpath')] " +
            // Button/link texts only in auth popup — header only has plain «Войти»
            "| //button[contains(.,'через Яндекс') or contains(.,'Яндекс ID') " +
            "or contains(.,'Войти с Яндекс')] " +
            "| //a[contains(.,'через Яндекс') or contains(.,'Яндекс ID')] " +
            // Toast / inline message telling the user to log in (UC-16 redirect case)
            "| //*[contains(.,'Войдите') or contains(.,'войдите') " +
            "or contains(.,'авторизуйтесь') or contains(.,'Авторизуйтесь')]" +
            "[not(child::*[contains(.,'Войдите') or contains(.,'войдите') " +
            "or contains(.,'авторизуйтесь')])]"

    fun waitShown(): Boolean = wait.until(
        ExpectedConditions.or(
            ExpectedConditions.presenceOfElementLocated(By.xpath(loginModal)),
            ExpectedConditions.urlContains("passport"),
            ExpectedConditions.urlContains("auth"),
            ExpectedConditions.urlContains("from="),
        ),
    )

    fun isShown(): Boolean =
        driver.findElements(By.xpath(loginModal)).isNotEmpty() ||
            driver.currentUrl.orEmpty().let {
                it.contains("passport") || it.contains("auth") || it.contains("from=")
            }
}
