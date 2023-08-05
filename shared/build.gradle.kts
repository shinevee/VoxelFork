plugins {
    java
}

val jomlVersion = "1.10.5"

repositories {
    mavenCentral()
}

dependencies {
	implementation("org.joml:joml:${jomlVersion}")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}