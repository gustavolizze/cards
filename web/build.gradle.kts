plugins {
    application
}


application {
    mainClass.set("com.cards.web.App")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.4.4")
    implementation(project(":core"))
    implementation(project(":database"))
}

