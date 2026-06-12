package boysband.selenium.tests

import boysband.selenium.pages.FilmPage
import boysband.selenium.support.BaseKinopoiskTest
import boysband.selenium.support.Browser
import boysband.selenium.support.Config
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource


class FilmInfoTest : BaseKinopoiskTest() {

    private val filmUrl = "${Config.baseUrl}/film/258687/"

    private fun openFilm(browser: Browser): FilmPage {
        open(browser, filmUrl)
        return FilmPage(driver, wait).also { it.waitLoaded() }
    }

    @ParameterizedTest(name = "UC-3: описание и рейтинг [{0}]")
    @MethodSource("boysband.selenium.support.BaseKinopoiskTest#browsers")
    fun descriptionAndRating(browser: Browser) {
        val film = openFilm(browser)
        assertTrue(film.title().isNotBlank(), "Должно отображаться название фильма")
        assertTrue(film.hasRating(), "Должен отображаться рейтинг")
        assertTrue(film.hasSynopsis(), "Должно отображаться описание")
    }

    @ParameterizedTest(name = "UC-4: состав и фильмография [{0}]")
    @MethodSource("boysband.selenium.support.BaseKinopoiskTest#browsers")
    fun castAndFilmography(browser: Browser) {
        val film = openFilm(browser)
        assertTrue(film.hasCast(), "На карточке фильма должен быть блок актёров")

        val person = film.openFirstCastMember()
        person.waitLoaded()
        assertTrue(person.hasFilmography(), "У выбранного актёра должна открываться фильмография")
    }

    @ParameterizedTest(name = "UC-5: просмотр трейлера [{0}]")
    @MethodSource("boysband.selenium.support.BaseKinopoiskTest#browsers")
    fun trailer(browser: Browser) {
        val film = openFilm(browser)
        assertTrue(film.hasTrailer(), "На карточке фильма должна быть кнопка трейлера")
        assertTrue(film.playTrailer(), "По клику на трейлер должен появляться плеер")
    }

    @ParameterizedTest(name = "UC-6: рецензии и отзывы [{0}]")
    @MethodSource("boysband.selenium.support.BaseKinopoiskTest#browsers")
    fun reviews(browser: Browser) {
        val film = openFilm(browser)
        assertTrue(film.hasReviewsLink(), "На карточке должна быть ссылка на рецензии")

        val reviews = film.openReviews()
        reviews.waitLoaded()
        assertTrue(reviews.hasReviews(), "У популярного фильма должны быть рецензии")
    }
}
