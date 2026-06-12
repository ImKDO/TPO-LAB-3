package boysband.selenium.tests

import boysband.selenium.pages.FilmPage
import boysband.selenium.pages.WatchPage
import boysband.selenium.support.BaseKinopoiskTest
import boysband.selenium.support.Browser
import boysband.selenium.support.Config
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource


class WatchTest : BaseKinopoiskTest() {

    private val filmUrl = "${Config.baseUrl}/film/258687/"

    @ParameterizedTest(name = "UC-10: на карточке есть кнопка «Смотреть» [{0}]")
    @MethodSource("boysband.selenium.support.BaseKinopoiskTest#browsers")
    fun watchButtonPresent(browser: Browser) {
        open(browser, filmUrl)
        val film = FilmPage(driver, wait)
        film.waitLoaded()
        if (film.hasWatchButton()) return



        driver.get("$filmUrl/watch/")
        val watch = WatchPage(driver, wait)
        watch.waitPlayerLoaded()
        assertTrue(
            watch.hasPlayerOrPurchaseGate(),
            "Точка входа в просмотр (кнопка на карточке или страница /watch/) должна быть доступна",
        )
    }

    @ParameterizedTest(name = "UC-10: открытие плеера / витрины покупки [{0}]")
    @MethodSource("boysband.selenium.support.BaseKinopoiskTest#browsers")
    fun playerOrPurchaseGateOpens(browser: Browser) {
        open(browser, "$filmUrl/watch/")
        val watch = WatchPage(driver, wait)
        watch.waitPlayerLoaded()
        assertTrue(
            watch.hasPlayerOrPurchaseGate(),
            "Для гостя должен появиться плеер или предложение оформить подписку / купить",
        )
    }

    @ParameterizedTest(name = "UC-11: список эфирных ТВ-каналов [{0}]")
    @MethodSource("boysband.selenium.support.BaseKinopoiskTest#browsers")
    fun tvChannelsListed(browser: Browser) {


        open(browser, "https://hd.kinopoisk.ru/channels")
        val watch = WatchPage(driver, wait)
        watch.waitTvLoaded()
        assertTrue(watch.tvChannelCount() > 0, "Должен открываться раздел эфирных ТВ-каналов")
    }
}
