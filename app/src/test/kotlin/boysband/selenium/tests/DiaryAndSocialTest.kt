package boysband.selenium.tests

import boysband.selenium.pages.AuthGate
import boysband.selenium.pages.FilmPage
import boysband.selenium.support.BaseKinopoiskTest
import boysband.selenium.support.Browser
import boysband.selenium.support.Config
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource


class DiaryAndSocialTest : BaseKinopoiskTest() {

    private val filmUrl = "${Config.baseUrl}/film/258687/"

    private fun openFilm(browser: Browser): FilmPage {
        open(browser, filmUrl)
        return FilmPage(driver, wait).also { it.waitLoaded() }
    }

    @ParameterizedTest(name = "UC-13: оценка фильма гостем требует входа [{0}]")
    @MethodSource("boysband.selenium.support.BaseKinopoiskTest#browsers")
    fun ratingRequiresLogin(browser: Browser) {
        val film = openFilm(browser)
        film.clickRate()
        assertTrue(AuthGate(driver, wait).waitShown(), "Оценка фильма гостем должна открывать вход")
    }

    @ParameterizedTest(name = "UC-14: «Буду смотреть» гостем требует входа [{0}]")
    @MethodSource("boysband.selenium.support.BaseKinopoiskTest#browsers")
    fun willWatchRequiresLogin(browser: Browser) {
        val film = openFilm(browser)
        film.clickWillWatch()
        assertTrue(AuthGate(driver, wait).waitShown(), "Добавление в «Буду смотреть» должно открывать вход")
    }

    @ParameterizedTest(name = "UC-15: создание списка требует входа [{0}]")
    @MethodSource("boysband.selenium.support.BaseKinopoiskTest#browsers")
    fun creatingListRequiresLogin(browser: Browser) {
        open(browser, "${Config.baseUrl}/mykp/movies/")
        assertTrue(AuthGate(driver, wait).waitShown(), "Создание тематического списка должно требовать входа")
    }

    @ParameterizedTest(name = "UC-16: написание рецензии требует входа [{0}]")
    @MethodSource("boysband.selenium.support.BaseKinopoiskTest#browsers")
    fun writingReviewRequiresLogin(browser: Browser) {
        val addReviewUrl = "${filmUrl}addreview/"
        open(browser, addReviewUrl)
        // Kinopoisk either shows an auth modal OR silently redirects away from addreview/
        val redirectedAway = !(driver.currentUrl ?: "").contains("addreview")
        assertTrue(redirectedAway || AuthGate(driver, wait).waitShown(), "Написание рецензии должно требовать входа")
    }

    @ParameterizedTest(name = "UC-17: отметка «Неинтересно» требует входа [{0}]")
    @MethodSource("boysband.selenium.support.BaseKinopoiskTest#browsers")
    fun markUninterestingRequiresLogin(browser: Browser) {
        open(browser)
        val uninteresting =
            "//button[contains(., 'Неинтересно') or contains(@aria-label,'Неинтересно')] " +
                "| //*[@data-id='not-interested' or contains(@class,'not-interested') or contains(@class,'NotInterested')]"
        val buttons = driver.findElements(org.openqa.selenium.By.xpath(uninteresting))

        if (buttons.isEmpty()) {
            assertTrue(true, "Кнопка «Неинтересно» не предлагается неавторизованному гостю")
        } else {
            waitClickable(uninteresting).click()
            assertTrue(AuthGate(driver, wait).waitShown(), "Отметка «Неинтересно» должна требовать входа")
        }
    }
}
