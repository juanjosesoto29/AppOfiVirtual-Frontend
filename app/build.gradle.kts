plugins {
    // Estas líneas usan el catálogo de versiones (libs.versions.toml), lo cual es correcto.
    // Si tu proyecto no lo tiene, las siguientes líneas también funcionarán.
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.ofivirtualapp"
    compileSdk = 34 // <-- CORRECTO: SDK Estable.

    defaultConfig {
        applicationId = "com.example.ofivirtualapp"
        minSdk = 24
        targetSdk = 34 // <-- CORRECTO: SDK Estable.
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    // La sección composeOptions debe estar DENTRO de android { ... }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1" // Versión compatible con Compose 1.6.x
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// --- SECCIÓN DE DEPENDENCIAS CORREGIDA ---
// Aquí reemplazamos las versiones alfa/beta por las estables recomendadas.
dependencies {
    // Core y Activity
    implementation("androidx.core:core-ktx:1.12.0") // Bajado de 1.17.0
    implementation("androidx.activity:activity-compose:1.8.2") // Bajado de 1.11.0

    // Lifecycle (ViewModel, etc.)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0") // Versión estable
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0") // Bajado de 2.9.4

    // Compose BOM (Bill of Materials) - Gestiona versiones de Compose
    implementation(platform("androidx.compose:compose-bom:2024.02.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.7.7") // Bajado de 2.9.5

    // Íconos (Las dos librerías para tener todos los íconos)
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended") // Ya la tenías, ahora funcionará

    // Dependencias de Test (sin cambios, generalmente)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("io.coil-kt:coil-compose:2.5.0")
}
