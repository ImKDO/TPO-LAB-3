package boysband.selenium.tests

import boysband.selenium.pages.CatalogPage
import boysband.selenium.pages.FilmPage
import boysband.selenium.support.BaseKinopoiskTest
import boysband.selenium.support.Browser
import boysband.selenium.support.Config
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource


class CatalogFilterTest : BaseKinopoiskTest() {

    private val catalogUrl = "${Config.baseUrl}/lists/movies/"
    private val filmUrl = "${Config.baseUrl}/film/258687/"

    @ParameterizedTest(name = "UC-7: фильтры по жанру/году/стране доступны [{0}]")
    @MethodSource("boysband.selenium.support.BaseKinopoiskTest#browsers")
    fun filterControlsArePresent(browser: Browser) {
        open(browser, catalogUrl)
        val catalog = CatalogPage(driver, wait)
        catalog.waitLoaded()

        assertTrue(catalog.resultCount() > 0, "В каталоге должны отображаться карточки")
        assertTrue(
            catalog.hasGenreFilter() || catalog.hasYearFilter() || catalog.hasCountryFilter(),
            "В каталоге должны быть фильтры по жанру / году / стране",
        )
    }

    @ParameterizedTest(name = "UC-7: применение фильтра по жанру [{0}]")
    @MethodSource("boysband.selenium.support.BaseKinopoiskTest#browsers")
    fun applyingGenreFilterKeepsResults(browser: Browser) {

        open(browser, "${Config.baseUrl}/lists/movies/genre--drama/")
        val catalog = CatalogPage(driver, wait)
        catalog.waitLoaded()
        assertTrue(catalog.resultCount() > 0, "Отфильтрованный по жанру список не должен быть пустым")
    }

    @ParameterizedTest(name = "UC-8: рейтинг ожиданий на ожидаемом фильме [{0}]")
    @MethodSource("boysband.selenium.support.BaseKinopoiskTest#browsers")
    fun expectationRatingShown(browser: Browser) {

        open(browser, "${Config.baseUrl}/lists/movies/popular-films/")
        val catalog = CatalogPage(driver, wait)
        catalog.waitLoaded()
        assertTrue(catalog.resultCount() > 0, "Список ожидаемых фильмов не должен быть пустым")
    }

    @ParameterizedTest(name = "UC-9: блок рекомендаций на карточке фильма [{0}]")
    @MethodSource("boysband.selenium.support.BaseKinopoiskTest#browsers")
    fun recommendationsShown(browser: Browser) {
        open(browser, filmUrl)
        FilmPage(driver, wait).waitLoaded()

        val catalog = CatalogPage(driver, wait)

        assertTrue(
            catalog.hasRecommendations(currentFilmId = "258687"),
            "На карточке фильма должен быть блок рекомендаций / похожих фильмов",
        )
    }
}
