plugins {
    application
    id("io.spring.dependency-management") version "1.1.3"
    id("com.github.ben-manes.versions") version "0.51.0"
    war
}

group = "blog-app"
version = "1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

application { mainClass.set("ru.goncharenko.blog.BlogApplication") }

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework:spring-framework-bom:6.2.15")
        mavenBom("org.springframework.data:spring-data-bom:2025.0.7")
        mavenBom("com.fasterxml.jackson:jackson-bom:2.20.1")
        mavenBom("org.junit:junit-bom:5.10.0")
    }
}

dependencies {
    val mapstructVersion: String by project.properties
    val lombokVersion: String by project.properties

    // Spring framework
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-webmvc")
    implementation("org.springframework.data:spring-data-jdbc")

    // PostgreSQL
    implementation("org.postgresql:postgresql:42.7.8")

    // JSON (для REST API)
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.core:jackson-core")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")

    // Servlet API
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")

    // Lombok
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    // MapStruct
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")

    // Validation API
    implementation("org.hibernate:hibernate-validator:8.0.1.Final")

    // Spring test framework
    testImplementation("org.springframework:spring-test")

    // JUnit framework
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    // Test DB
    testImplementation("com.h2database:h2:2.3.232")

    // Usefull tests libraries
    testImplementation("jakarta.servlet:jakarta.servlet-api:6.1.0")
    testImplementation("org.glassfish:jakarta.el:4.0.2")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("com.jayway.jsonpath:json-path:2.9.0")
}

tasks.war {
    archiveFileName.set("ROOT.war")
}

tasks {
    test {
        useJUnitPlatform()
    }

    compileJava {
        options.compilerArgs.addAll(
            listOf("-Amapstruct.defaultComponentModel=spring")
        )
    }
}
