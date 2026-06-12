package boysband.selenium.support

import java.time.Duration


object Config {

    
    val baseUrl: String = prop("selenium.baseUrl", "https://www.kinopoisk.ru")

    
    val headless: Boolean = prop("selenium.headless", "true").toBoolean()

    
    val implicitWait: Duration = Duration.ofSeconds(prop("selenium.implicitWaitSec", "1").toLong())

    
    val explicitWait: Duration = Duration.ofSeconds(prop("selenium.explicitWaitSec", "8").toLong())

    
    val browsers: List<Browser> =
        prop("selenium.browsers", "FIREFOX,CHROME")
            .split(',')
            .map { it.trim().uppercase() }
            .filter { it.isNotEmpty() }
            .map { Browser.valueOf(it) }

    private fun prop(key: String, default: String): String =
        System.getProperty(key)?.takeIf { it.isNotBlank() } ?: default
}
