
plugins {
    java
    application
    id("io.freefair.lombok") version "6.6.3"
    id("org.springframework.boot") version "2.7.5"
    //id("io.qameta.allure") version "2.10.0"
}


group = "ru.dimaggio"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

/*allure {
    version.set("2.10.0")
    adapter.autoconfigure.set(true)
}*/



dependencies {
    implementation("org.projectlombok:lombok:1.18.26")
    implementation(platform("org.junit:junit-bom:5.8.2"))
    implementation("io.rest-assured:rest-assured:5.4.0")
    implementation("io.rest-assured:json-path:5.4.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.7.5")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("org.hamcrest:hamcrest:2.2")
    implementation("org.springframework.boot:spring-boot-starter-test:2.7.5")

    implementation("org.postgresql:postgresql:42.6.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.10.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.10.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.10.1")

    // https://mvnrepository.com/artifact/io.qameta.allure/allure-junit5
    implementation("io.qameta.allure:allure-junit5:2.25.0")
    testImplementation(platform("io.qameta.allure:allure-bom:2.25.0"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.2.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.2.0")
    testImplementation("org.aspectj:aspectjtools:1.9.8")






}

tasks.test {
    useJUnitPlatform()
}