import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version "2.2.20"
}

val version = "1.0.1"
val buildInfoPackage = "com.taeyeon.xoduslol.buildinfo"
val buildInfoDir = layout.buildDirectory.dir("generated/sources/buildInfo/kotlin")

@CacheableTask
abstract class GenerateBuildInfoTask : DefaultTask() {
    @get:Input
    abstract val versionProp: Property<String>

    @get:Input
    abstract val pkgProp: Property<String>

    @get:OutputDirectory
    abstract val outDir: DirectoryProperty

    @TaskAction
    fun generate() {
        val buildNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        val out = outDir.get().asFile
        val pkgPath = pkgProp.get().replace('.', '/')
        val file = File(out, "$pkgPath/BuildInfo.kt")
        file.parentFile.mkdirs()
        file.writeText(
            """
            package ${pkgProp.get()}
            
            object BuildInfo {
                const val VERSION = "${versionProp.get()}"
                const val BUILD_NUMBER  = "$buildNumber"
            }
            """.trimIndent()
        )
        println("Generated BuildInfo.kt at: ${file.absolutePath}")
    }
}

val generateBuildInfo by tasks.register<GenerateBuildInfoTask>("generateBuildInfo") {
    versionProp.set(version)
    pkgProp.set(buildInfoPackage)
    outDir.set(buildInfoDir)
}

kotlin.sourceSets.getByName("commonMain").kotlin.srcDir(buildInfoDir)

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.9.0-rc01")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
            implementation("io.github.vinceglb:confettikit:0.6.0")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }

    targets.all {
        compilations.all {
            compileTaskProvider.configure { dependsOn(generateBuildInfo) }
        }
    }
}
