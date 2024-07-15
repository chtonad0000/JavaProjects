plugins {
    id("java")
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.Chub"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation ("org.hibernate:hibernate-core:5.5.7.Final")
    compileOnly ("org.projectlombok:lombok:1.18.20")
    annotationProcessor ("org.projectlombok:lombok:1.18.20")
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation(project(":DataModule"))
}

tasks.test {
    useJUnitPlatform()
}