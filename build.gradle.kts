plugins {
	java
	id("org.springframework.boot") version "3.4.4" apply false
	id("io.spring.dependency-management") version "1.1.7"
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

allprojects {
	group = "com.cards"
	version = "0.0.1-SNAPSHOT"

	repositories {
		mavenCentral()
	}


}

subprojects {

	apply {
		plugin("java")
		plugin("io.spring.dependency-management")
		plugin("org.springframework.boot")
	}

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter");
		compileOnly("org.projectlombok:lombok")
		annotationProcessor("org.projectlombok:lombok")

		implementation("org.flywaydb:flyway-core:10.12.0")
		implementation("org.flywaydb:flyway-mysql:10.12.0")
		implementation("io.r2dbc:r2dbc-pool")
		implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
		runtimeOnly("io.asyncer:r2dbc-mysql")
		implementation("com.mysql:mysql-connector-j:9.2.0")

		implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")
		implementation("org.springframework.boot:spring-boot-starter-webflux")
		implementation("org.springframework.boot:spring-boot-starter-validation")
		implementation("org.springframework.boot:spring-boot-docker-compose")
		
		testImplementation("org.springframework.boot:spring-boot-starter-test")
		testImplementation("io.projectreactor:reactor-test")
		testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}

	tasks.getByName("bootJar") {
		enabled = false
	}

	tasks.getByName("jar") {
		enabled = true
	}
}
