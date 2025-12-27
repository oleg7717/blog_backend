plugins {
    application
    id("io.spring.dependency-management") version "1.1.3"
    id("com.github.ben-manes.versions") version "0.51.0"
}

group = "blog-backend"
version = "1.0-SNAPSHOT"

application { mainClass.set("ru.goncharenko.blog.BlogApplication") }

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework:spring-framework-bom:6.2.15")
        mavenBom("org.junit:junit-bom:5.10.0")
    }
}

dependencies {
    val mapstructVersion: String by project.properties
    val lombokVersion: String by project.properties

    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-webmvc")
    implementation("org.springframework.data:spring-data-jdbc:3.5.7")

    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")

    testImplementation("org.springframework:spring-test")
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
