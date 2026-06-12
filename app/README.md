# Selenium-тесты Кинопоиска (`:selenium`)

Автоматизированное тестовое покрытие набора прецедентов
[kinopoisk.ru](https://www.kinopoisk.ru) (см. `TASK.md`) средствами **Selenium WebDriver 4**.
Каждый прецедент покрыт параметризованным тестом, который выполняется в **Firefox** и **Chrome**.

## Соответствие ТЗ

| Пункт ТЗ | Как выполнено |
|----------|---------------|
| Покрытие на основе прецедентов kinopoisk.ru | Тесты на UC-1…UC-17, сгруппированные по веткам use-case-диаграммы |
| Автоматизация через Selenium | Selenium WebDriver 4 + JUnit 5 |
| Шаблоны Selenium IDE, исполнение в Firefox и Chrome | Каждый тест — `@ParameterizedTest` по `enum Browser { FIREFOX, CHROME }` |
| Динамический DOM → выбор по XPath, а не по ID | Все локаторы в Page Object'ах заданы **только через XPath** |

> **Про Selenium RC.** В ТЗ упомянут Selenium RC. Он объявлен устаревшим ещё в Selenium 2 и
> удалён из современных дистрибутивов и Maven Central — несовместим с актуальными браузерами и
> драйверами. Используется его прямой преемник, **Selenium WebDriver 4**. Драйверы (geckodriver /
> chromedriver) скачиваются автоматически встроенным **Selenium Manager**, отдельный сервер RC не
> нужен.

## Карта «прецедент → тест»

| UC | Прецедент | Тест |
|----|-----------|------|
| —     | Открывается главная страница | `HomeAndSearchTest.homePageLoads` |
| UC-1  | Поиск по названию фильма | `HomeAndSearchTest.searchByFilmTitle` |
| UC-2  | Поиск по актёру / режиссёру | `HomeAndSearchTest.searchByPerson` |
| UC-3  | Просмотр описания и рейтинга | `FilmInfoTest.descriptionAndRating` |
| UC-4  | Просмотр состава и фильмографии | `FilmInfoTest.castAndFilmography` |
| UC-5  | Просмотр трейлера | `FilmInfoTest.trailer` |
| UC-6  | Чтение рецензий и отзывов | `FilmInfoTest.reviews` |
| UC-7  | Фильтрация по жанру / году / стране | `CatalogFilterTest.filterControlsArePresent`, `applyingGenreFilterKeepsResults` |
| UC-8  | Просмотр прогнозной оценки | `CatalogFilterTest.expectationRatingShown` |
| UC-9  | Просмотр рекомендаций | `CatalogFilterTest.recommendationsShown` |
| UC-10 | Просмотр фильма | `WatchTest.watchButtonPresent`, `playerOrPurchaseGateOpens` |
| UC-11 | Просмотр эфирного ТВ | `WatchTest.tvChannelsListed` |
| UC-13 | Выставление оценки фильму | `DiaryAndSocialTest.ratingRequiresLogin` |
| UC-14 | Добавление в «Буду смотреть» | `DiaryAndSocialTest.willWatchRequiresLogin` |
| UC-15 | Создание тематического списка | `DiaryAndSocialTest.creatingListRequiresLogin` |
| UC-16 | Написание рецензии | `DiaryAndSocialTest.writingReviewRequiresLogin` |
| UC-17 | Отметка «Неинтересно» | `DiaryAndSocialTest.markUninterestingRequiresLogin` |

> **UC-12** в текстовой части диаграммы только упомянут (`UC_watch ..> UC12`), но самого прецедента
> без описания нет — поэтому он не покрывается.
>
> **UC-13…UC-17** в диаграмме исходят от единственного актёра «Гость», но это персональные
> действия. Для неавторизованного гостя корректное наблюдаемое поведение — появление окна входа
> Яндекс ID. Тесты проверяют именно это (детерминированно, без реального аккаунта).

## Архитектура

```
src/test/kotlin/boysband/selenium/
├── support/
│   ├── Browser.kt            # enum: FIREFOX, CHROME
│   ├── Config.kt             # baseUrl, таймауты, набор браузеров (через -D…)
│   ├── DriverFactory.kt      # создание/настройка драйвера (ru-RU, headless, анти-детект)
│   └── BaseKinopoiskTest.kt  # жизненный цикл, баннер cookie, XPath-хелперы, MethodSource браузеров
├── pages/                    # Page Object'ы, локаторы только по XPath
│   ├── MainPage / SearchResultsPage / NamePage
│   ├── FilmPage / ReviewsPage
│   ├── CatalogPage / WatchPage
│   └── AuthGate              # окно авторизации для UC-13…UC-17
└── tests/                    # тесты по группам прецедентов
```

## Запуск

```bash
# Все тесты в Firefox и Chrome (по умолчанию, headless)
./gradlew :selenium:test

# Headful — посмотреть, как браузер прокликивает сценарии
./gradlew :selenium:test -Dselenium.headless=false

# Только один браузер
./gradlew :selenium:test -Dselenium.browsers=CHROME
```

Требуется локально установленный Firefox и/или Chrome. Драйверы подтянет Selenium Manager.

### Важно про прогон на «живом» сайте

kinopoisk.ru использует защиту от ботов (SmartCaptcha) и блокировку по IP. Сама инфраструктура
тестов проверена и работает: Selenium Manager сам скачивает chromedriver/geckodriver, браузер
стартует и открывает страницу. Но при прогоне из датацентра / через VPN сайт отдаёт страницу-заглушку
«It looks like you're using a VPN» (`variant: block`) вместо контента — поэтому ассерты не проходят.
Это ограничение сетевого окружения, а не сценариев.

Чтобы тесты проходили на «живом» сайте, запускать нужно:

* с обычного (резидентного) IP, без VPN/прокси;
* при необходимости — с ручным проходом капчи в headful-режиме (`-Dselenium.headless=false`).

Локаторы намеренно сделаны устойчивыми (XPath по видимому тексту/ссылкам, а не по ID), чтобы пережить
динамическую перерисовку DOM. Для стабильного CI сайт обычно проксируют через тестовый стенд/моки.
