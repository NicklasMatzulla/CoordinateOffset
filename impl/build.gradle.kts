plugins {
    id("com.gradleup.shadow")
}

dependencies {
    implementation(project(":api"))
    compileOnly(libs.paper.api)

    testImplementation(libs.paper.api)
    testImplementation(libs.test.junit.jupiter)
    testRuntimeOnly(libs.test.junit.platform)
}

tasks {
    processResources {
        inputs.property("apiVersion", libs.versions.paper.apiversion)
        val placeholders = mapOf(
            "version" to version.toString(),
            "apiVersion" to libs.versions.paper.apiversion.get(),
        )
        placeholders.forEach { (k, v) -> inputs.property(k, v) } // ensure cache is invalidated after version bumps
        files(listOf("paper-plugin.yml")) {
            expand(placeholders)
        }
    }

    jar {
        enabled = false
    }

    shadowJar {
        archiveBaseName.set("CoordinateOffset-Paper")
        archiveClassifier.set("")
    }

    register<Copy>("buildSnapshot") {
        // Copy the latest artifact from `assemble` task to a consistent place for symlinking into a server.
        dependsOn(shadowJar)
        from(shadowJar)
        into("build")
        val projectName = rootProject.name
        rename { "$projectName-Paper-SNAPSHOT.jar" }
    }

    assemble {
        dependsOn(shadowJar)
    }

    test {
        useJUnitPlatform()
    }
}
