import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.2.6.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("jvm") version "1.3.71"
	kotlin("plugin.spring") version "1.3.71"
}

group = "com.archesky.auth.server"
version = "1.3.0"
java.sourceCompatibility = JavaVersion.VERSION_11

val developmentOnly by configurations.creating
configurations {
	runtimeClasspath {
		extendsFrom(developmentOnly)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.graphql-java-kickstart:graphql-spring-boot-starter:7.1.0")
	implementation("com.graphql-java-kickstart:graphql-kickstart-spring-boot-starter-tools:7.1.0")
	implementation("com.auth0:java-jwt:3.10.2")
	implementation("com.auth0:jwks-rsa:0.11.0")
	runtimeOnly("com.graphql-java-kickstart:altair-spring-boot-starter:7.1.0")
	runtimeOnly("com.graphql-java-kickstart:graphiql-spring-boot-starter:7.1.0")
	runtimeOnly("com.graphql-java-kickstart:voyager-spring-boot-starter:7.1.0")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}
