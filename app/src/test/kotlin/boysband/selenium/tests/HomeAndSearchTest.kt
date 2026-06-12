package boysband.selenium.tests

import boysband.selenium.pages.MainPage
import boysband.selenium.support.BaseKinopoiskTest
import boysband.selenium.support.Browser
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource


class HomeAndSearchTest : BaseKinopoiskTest() {

    @ParameterizedTest(name = "Главная страница загружается [{0}]")
    @MethodSource("boysband.selenium.support.BaseKinopoiskTest#browsers")
    fun homePageLoads(browser: Browser) {
        open(browser)
        val main = MainPage(driver, wait)
        assertTrue(main.isLoaded(), "На главной странице должна быть строка поиска")
    }

    @ParameterizedTest(name = "UC-1: поиск по названию фильма [{0}]")
    @MethodSource("boysband.selenium.support.BaseKinopoiskTest#browsers")
    fun searchByFilmTitle(browser: Browser) {
        open(browser)
        val results = MainPage(driver, wait).search("Интерстеллар")
        results.waitLoaded()
        assertTrue(results.hasFilmResults(), "Поиск по названию должен вернуть карточки фильмов")

        val film = results.openFirstFilm()
        film.waitLoaded()
        assertTrue(film.title().isNotBlank(), "Открытая карточка должна иметь название")
    }

    @ParameterizedTest(name = "UC-2: поиск по актёру / режиссёру [{0}]")
    @MethodSource("boysband.selenium.support.BaseKinopoiskTest#browsers")
    fun searchByPerson(browser: Browser) {
        open(browser)
        val results = MainPage(driver, wait).search("Кристофер Нолан")
        results.waitLoaded()
        assertTrue(results.hasNameResults(), "Поиск по персоне должен вернуть карточки людей")

        val name = results.openFirstName()
        name.waitLoaded()
        assertTrue(name.name().isNotBlank(), "Открытая карточка персоны должна иметь имя")
        assertTrue(name.hasFilmography(), "У персоны должна отображаться фильмография")
    }
}
