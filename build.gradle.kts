plugins {
    `java-library`
    `maven-publish`
    signing
}

group = "com.neovisionaries"
version = "3.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

java {
    // TODO: java-doc had many problems generating, am not willing to fix rn
    // withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            // Inherits group and version from toplevel
            artifactId = "nv-websocket-client"

            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }

            pom {
                name = "${groupId}:${artifactId}"
                description = "WebSocket client implementation in Java."
                url = "https://github.com/ToxicMushroom/nv-websocket-client"

                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }

                developers {
                    developers {
                        name = "Takahiko Kawasaki"
                    }
                }
            }
        }

        repositories {
            maven {
                val releasesRepoUrl = "https://reposilite.melijn.com/releases"
                val snapshotsRepoUrl = "https://reposilite.melijn.com/snapshots"
                url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
                credentials {
                    username = project.findProperty("melijnReposilitePub").toString()
                    password = project.findProperty("melijnReposilitePassword").toString()
                }
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["mavenJava"])
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}