import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFrameworkTask
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    android()

    val iosX64 = this.iosX64()
    val iosArm64 = this.iosArm64()
    val iosSimulatorArm64 = this.iosSimulatorArm64()

    listOf(
        iosX64,
        iosArm64,
        iosSimulatorArm64,
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    tasks.register<XCFrameworkTask>("assembleReleaseXCFrameworkForSPM") {
        description = """
        Assemble a release configuration XCFramework for Swift Package Manager.
        Build single simulator target (x64 or simulatorArm64) and device target (arm64) to reduce total build time. 
        """.trimIndent()

        listOf(
            iosArm64,
            if (System.getProperty("os.arch") == "aarch64")
                iosSimulatorArm64
            else
                iosX64
        ).forEach {
            from(it.binaries.getFramework(NativeBuildType.RELEASE))
        }
    }
    
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "jp.martysuzuki.multiplatformbuildtool"
    compileSdk = 32
    defaultConfig {
        minSdk = 31
        targetSdk = 32
    }
}