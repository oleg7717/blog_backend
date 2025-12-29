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

    // Servlet API
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")

    // Lombok
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    // MapStruct
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")

    // Spring framework
    testImplementation("org.springframework:spring-test")
    testImplementation("org.junit.jupiter:junit-jupiter")
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
