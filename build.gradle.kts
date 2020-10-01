import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.lang.System.getenv

plugins {
	id("org.springframework.boot") version "2.3.4.RELEASE"
	id("io.spring.dependency-management") version "1.0.10.RELEASE"
	`maven-publish`
	kotlin("jvm") version "1.3.72"
	kotlin("plugin.spring") version "1.3.72"
}

buildscript {
	tasks.findByName("build")?.dependsOn("bootJar")
}

group = "com.archesky.auth.server"
version = "0.0.${getenv().getOrDefault("GITHUB_RUN_ID", "1")}-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	jcenter()
	maven("https://maven.pkg.github.com/Rich43/archesky-common-library") {
		credentials {
			username = "Rich43"
			password = getenv()["GITHUB_TOKEN"]
		}
	}
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
	implementation("com.google.guava:guava:29.0-jre")
	implementation("com.archesky.common.library:archesky-common-library:0.0.277372077-SNAPSHOT")
	runtimeOnly("com.graphql-java-kickstart:altair-spring-boot-starter:7.1.0")
	runtimeOnly("com.graphql-java-kickstart:graphiql-spring-boot-starter:7.1.0")
	runtimeOnly("com.graphql-java-kickstart:voyager-spring-boot-starter:7.1.0")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("com.graphql-java-kickstart:graphql-spring-boot-test:7.1.0")
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

publishing {
	repositories {
		maven {
			name = "GitHubPackages"
			url = uri("https://maven.pkg.github.com/Rich43/archesky-auth-server")
			credentials {
				username = getenv().getOrDefault("GITHUB_ACTOR", "Rich43")
				password = getenv()["GITHUB_TOKEN"]
			}
		}
	}
	publications {
		create<MavenPublication>("gpr") {
			from(components["java"])
			artifact(tasks.getByName("bootJar"))
		}
	}
}
