import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Date
import java.text.SimpleDateFormat
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
	maven // only applied to make bintray happy
	`maven-publish`
	`java-gradle-plugin`
	id("org.springframework.boot") version "2.2.6.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	id("com.jfrog.bintray") version "1.8.5"
	id("com.gradle.plugin-publish") version "0.11.0"
	id("com.github.johnrengelman.shadow") version "2.0.2"
	kotlin("jvm") version "1.3.71"
	kotlin("plugin.spring") version "1.3.71"
}

group = "com.pynguins.auth"
version = "1.3.0"
val githubURL = "https://github.com/Rich43/${rootProject.name}.git"
val description = "Pynguins OpenID Connect token validation and extraction server"
java.sourceCompatibility = JavaVersion.VERSION_11


val developmentOnly by configurations.creating
configurations {
	runtimeClasspath {
		extendsFrom(developmentOnly)
	}
}

repositories {
	mavenCentral()
	jcenter()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.graphql-java-kickstart:graphql-spring-boot-starter:6.0.0")
	implementation("com.graphql-java-kickstart:graphql-kickstart-spring-boot-starter-tools:6.0.0")
	implementation("com.auth0:java-jwt:3.10.2")
	implementation("com.auth0:jwks-rsa:0.11.0")
	runtimeOnly("com.graphql-java-kickstart:altair-spring-boot-starter:6.0.0")
	runtimeOnly("com.graphql-java-kickstart:graphiql-spring-boot-starter:6.0.0")
	runtimeOnly("com.graphql-java-kickstart:voyager-spring-boot-starter:6.0.0")
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

configure<com.jfrog.bintray.gradle.BintrayExtension> {
	user = System.getenv("BINTRAY_USER")
	key = System.getenv("BINTRAY_KEY")
	pkg.apply {
		repo = "pynguins"
		name = rootProject.name
		userOrg = "pynguins"
		setLicenses("BSD")
		vcsUrl = githubURL
		issueTrackerUrl = "https://github.com/Rich43/${rootProject.name}/issues"
		publish = true
		publicDownloadNumbers = true

		version.apply {
			name = project.version.toString()
			desc = "${rootProject.name} ${project.version}"
			vcsTag = project.version.toString()
			released = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ").format(Date())
		}
	}
}

val sourcesJar by tasks.creating(Jar::class) {
	dependsOn("classes")
	classifier = "sources"
	from(sourceSets["main"].allSource)
}

val shadowJar: ShadowJar by tasks
shadowJar.apply {
	baseName = rootProject.name
	classifier = null
	isZip64 = true
}

publishing {
	publications {
		register("mavenJava", MavenPublication::class) {
			// from(components["java"])
			artifactId = rootProject.name
			artifact(shadowJar)
			artifact(sourcesJar) { classifier = "sources" }
			pom.withXml {
				asNode().apply {
					appendNode("name", rootProject.name)
					appendNode("description", description)
					appendNode("licenses").appendNode("license").apply {
						appendNode("name", "BSD 2-Clause License")
						appendNode("url", "https://choosealicense.com/licenses/bsd-2-clause/")
						appendNode("distribution", "repo")
					}
					appendNode("developers").appendNode("developer").apply {
						appendNode("id", "Rich43")
						appendNode("name", "Richard Ward")
					}
					appendNode("scm").apply {
						appendNode("url", githubURL)
					}
					appendNode("dependencies").let { depNode ->
						configurations.compile.allDependencies.forEach {
							depNode.appendNode("dependency").apply {
								appendNode("groupId", it.group)
								appendNode("artifactId", it.name)
								appendNode("version", it.version)
							}
						}
					}
				}
			}
		}
	}
}

tasks["bintrayUpload"].dependsOn("publishToMavenLocal")
