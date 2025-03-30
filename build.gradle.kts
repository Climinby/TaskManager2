plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.beryx.jlink") version "2.26.0"
}

group = "com.climinby.aqiiv"
version = "2.1.1"

application {
    mainModule.set("TaskManager.main")
    mainClass.set("com.climinby.aqiiv.Main")
}

jlink {
    imageDir.set(layout.buildDirectory.dir("image"))
    launcher {
        name = "TaskManager2"
    }
    jpackage {
        installerType = "exe"
        skipInstaller = true
//        outputDir = layout.buildDirectory.dir("dist").get().asFile.absolutePath
        outputDir = "dist"
        mainClass.set("com.climinby.aqiiv.Main")
    }
}

tasks.named<JavaExec>("run") {
    dependsOn(tasks.shadowJar)
    classpath = files(tasks.shadowJar.get().archiveFile)
}

tasks.named("distZip") {
    dependsOn(tasks.shadowJar)
}

tasks.named("distTar") {
    dependsOn(tasks.shadowJar)
}

tasks.named("startScripts") {
    dependsOn(tasks.shadowJar)
}

tasks.named("startShadowScripts") {
    dependsOn(tasks.jar)
}

tasks.shadowJar {
    archiveClassifier.set("")
    manifest {
        attributes["Main-Class"] = "com.climinby.aqiiv.Main"
    }
}

//tasks.jar {
//    manifest {
//        attributes["Main-Class"] = "com.climinby.aqiiv.Main"
//    }
//}

javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.datatype/jackson-datatype-jsr310
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.2")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.test {
    useJUnitPlatform()
}