import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
}

group = "kern"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

val testcontainersVersion = "1.19.7"

dependencyManagement {
	imports {
        mavenBom("org.testcontainers:testcontainers-bom:$testcontainersVersion")
	}
}

repositories {
    maven { url = uri("https://repo.spring.io/milestone") } //spring-data-elasticsearch-m2
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
    implementation("org.springframework.data:spring-data-elasticsearch:5.3.0-M2")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:elasticsearch:$testcontainersVersion")
	testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.5.5")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
	jvmArgs("-XX:+EnableDynamicAgentLoading")
}
