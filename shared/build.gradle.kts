import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.kotlinSerialization)
}

kotlin {
  androidTarget { @OptIn(ExperimentalKotlinGradlePluginApi::class) compilerOptions { jvmTarget.set(JvmTarget.JVM_11) } }

  listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
    iosTarget.binaries.framework {
      baseName = "Shared"
      isStatic = true
    }
  }

  sourceSets {
    commonMain.dependencies { implementation(libs.kotlinx.serialization.json) }
    commonTest.dependencies { implementation(libs.kotlin.test) }
  }
}

android {
  namespace = "no.ordbokene.shared"
  compileSdk = libs.versions.android.compileSdk.get().toInt()
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  defaultConfig { minSdk = libs.versions.android.minSdk.get().toInt() }
  sourceSets["test"].resources { srcDirs("src/commonTest/resources") }
}

tasks.register<Copy>("copyiOSTestResources") {
  from("src/commonTest/resources")
  into("build/bin/iosSimulatorArm64/debugTest/resources")
}

tasks.findByName("iosSimulatorArm64Test")!!.dependsOn("copyiOSTestResources")

tasks.withType<Test>().configureEach { jvmArgs("-Xmx1G") }
