
allprojects {
    repositories {
        jcenter()
    }
}

plugins {
    id("org.springframework.boot") version "2.3.1.RELEASE" apply false
    id("io.spring.dependency-management") version "1.0.9.RELEASE" apply false
    kotlin("jvm") version "1.3.72" apply false
    kotlin("plugin.spring") version "1.3.72" apply false
    kotlin("plugin.jpa") version "1.3.72" apply false
}

subprojects {
    version = "0.0.1-SNAPSHOT"
}

tasks.register<Copy>("copyFrontend") {
    from("frontend/public/")
    into("backend/build/resources/main/public/")
}
