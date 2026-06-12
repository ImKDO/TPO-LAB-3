package boysband.selenium.support

import org.openqa.selenium.PageLoadStrategy
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions


object DriverFactory {

    fun create(browser: Browser): WebDriver = when (browser) {
        Browser.CHROME -> ChromeDriver(chromeOptions())
        Browser.FIREFOX -> FirefoxDriver(firefoxOptions())
    }

    private fun chromeOptions(): ChromeOptions = ChromeOptions().apply {


        setPageLoadStrategy(PageLoadStrategy.EAGER)
        if (Config.headless) addArguments("--headless=new")
        addArguments(
            "--window-size=1920,1080",
            "--lang=ru-RU",
            "--no-sandbox",
            "--disable-dev-shm-usage",

            "--disable-blink-features=AutomationControlled",
        )
        setExperimentalOption("excludeSwitches", listOf("enable-automation"))
    }

    private fun firefoxOptions(): FirefoxOptions = FirefoxOptions().apply {
        setPageLoadStrategy(PageLoadStrategy.EAGER)
        if (Config.headless) addArguments("-headless")
        addArguments("--width=1920", "--height=1080")
        addPreference("intl.accept_languages", "ru-RU")
        addPreference("dom.webdriver.enabled", false)
    }
}
