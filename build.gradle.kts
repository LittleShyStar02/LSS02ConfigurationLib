import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SourcesJar

plugins {
    id("com.vanniktech.maven.publish") version "0.37.0" apply false
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }
}

subprojects {
    apply(plugin = "java-library")

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    dependencies {
        "testImplementation"(platform("org.junit:junit-bom:6.1.3"))
        "testImplementation"("org.junit.jupiter:junit-jupiter")
        "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
    }

    afterEvaluate {
        configure<MavenPublishBaseExtension> {
            configure(JavaLibrary(javadocJar = JavadocJar.Javadoc(), sourcesJar = SourcesJar.Sources()))
            publishToMavenCentral(automaticRelease = false)
            signAllPublications()

            pom {
                name.set(project.name)
                description.set(project.description)
                url.set("https://github.com/LittleShyStar02/LSS02ConfigurationLib")

                licenses {
                    license {
                        name.set("PolyForm Noncommercial License 1.0.0")
                        url.set("https://polyformproject.org/licenses/noncommercial/1.0.0")
                    }
                }

                developers {
                    developer {
                        id.set("LittleShyStar02")
                        name.set("Vincenzo Cutolo")
                        url.set("https://github.com/LittleShyStar02")
                    }
                }

                scm {
                    url.set("https://github.com/LittleShyStar02/LSS02ConfigurationLib")
                    connection.set("scm:git:https://github.com/LittleShyStar02/LSS02ConfigurationLib.git")
                    developerConnection.set("scm:git:git@github.com:LittleShyStar02/LSS02ConfigurationLib.git")
                }
            }
        }
    }
}
