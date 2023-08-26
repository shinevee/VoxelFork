import java.text.SimpleDateFormat
import java.util.*

plugins {
    application
}

val lwjglVersion = "3.3.2"
val jomlVersion = "1.10.5"
val lwjglNatives = "natives-windows"

repositories {
    mavenCentral()
}

dependencies {
	implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

	implementation("org.lwjgl:lwjgl")
	implementation("org.lwjgl:lwjgl-assimp")
	implementation("org.lwjgl:lwjgl-glfw")
	implementation("org.lwjgl:lwjgl-openal")
	implementation("org.lwjgl:lwjgl-opengl")
	implementation("org.lwjgl:lwjgl-stb")
	runtimeOnly("org.lwjgl:lwjgl::$lwjglNatives")
	runtimeOnly("org.lwjgl:lwjgl-assimp::$lwjglNatives")
	runtimeOnly("org.lwjgl:lwjgl-glfw::$lwjglNatives")
	runtimeOnly("org.lwjgl:lwjgl-openal::$lwjglNatives")
	runtimeOnly("org.lwjgl:lwjgl-opengl::$lwjglNatives")
	runtimeOnly("org.lwjgl:lwjgl-stb::$lwjglNatives")
	implementation("org.joml:joml:${jomlVersion}")

	implementation(project(":shared"))
}

application {
    mainClass.set("io.bluestaggo.voxelthing.Game")
}

tasks.compileJava {
	dependsOn("genMetadata")
}

task("genMetadata") {
	val resources = sourceSets.main.get().output.resourcesDir
	resources?.mkdirs()

	val versionFile = file("$resources/version.txt")
	versionFile.createNewFile()
	versionFile.writeText(getaVersion())
}

fun getaVersion(): String {
    var version = providers.gradleProperty("voxelthing.version").get();
	version = version.replace("dev", "dev ${SimpleDateFormat("yyyyMMdd").format(Date())}");
	return version;
}
