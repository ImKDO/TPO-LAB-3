plugins {
    id("buildsrc.convention.kotlin-jvm")
    application
}

dependencies {
    implementation(project(":utils"))
    testImplementation(libs.bundles.selenium)
    testImplementation(kotlin("test"))
    testRuntimeOnly(libs.junitPlatformLauncher)
}

application {
    mainClass = "boysband.app.AppKt"
}

tasks.withType<Test>().configureEach {
    maxParallelForks = 1
    systemProperty("selenium.headless", providers.systemProperty("selenium.headless").getOrElse("true"))
    listOf("selenium.browsers", "selenium.baseUrl", "selenium.implicitWaitSec", "selenium.explicitWaitSec")
        .forEach { key -> providers.systemProperty(key).orNull?.let { systemProperty(key, it) } }
}
