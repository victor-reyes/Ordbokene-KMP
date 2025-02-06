import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.kotlinSerialization)
  alias(libs.plugins.ksp)
  alias(libs.plugins.nativeCoroutines)
}

kotlin {
  androidTarget { @OptIn(ExperimentalKotlinGradlePluginApi::class) compilerOptions { jvmTarget.set(JvmTarget.JVM_11) } }

  listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
    iosTarget.binaries.framework {
      baseName = "Shared"
      isStatic = false
    }
  }

  sourceSets {
    commonMain.dependencies {
      implementation(libs.kotlinx.serialization.json)
      implementation(libs.kotlinx.coroutines.core)
      implementation(libs.ktor.client.core)
      implementation(libs.ktor.client.content.negotiation)
      implementation(libs.ktor.serialization.kotlinx.json)
      implementation(libs.ktor.client.logging)
      implementation(libs.napier)

      implementation(project.dependencies.platform(libs.koin.bom))
      implementation(libs.koin.core)
    }
    commonTest.dependencies { implementation(libs.kotlin.test) }
    androidMain.dependencies { implementation(libs.ktor.client.okhttp) }
    iosMain.dependencies { implementation(libs.ktor.client.darwin) }

    all { languageSettings.optIn("kotlin.experimental.ExperimentalObjCName") }
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
