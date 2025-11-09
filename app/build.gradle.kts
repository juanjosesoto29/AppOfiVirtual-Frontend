plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.0.21-1.0.25"

}

android {
    namespace = "com.example.ofivirtualapp"
    compileSdk = 36 // <-- CORRECTO: SDK Estable.

    defaultConfig {
        applicationId = "com.example.ofivirtualapp"
        minSdk = 24
        targetSdk = 36 // <-- CORRECTO: SDK Estable.
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation(libs.androidx.room.common.jvm)
    // Se define una versión consistente para todo el ciclo de vida (Lifecycle)    def lifecycle_version = "2.7.0" // Todas las librerías de lifecycle usarán ESTA versión.
    val lifecycle_version = "2.7.0"
    // --- CORE Y ACTIVITY ---
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.activity:activity-compose:1.8.2")


    // --- LIFECYCLE (VIEWMODEL, ETC.) - ¡CORREGIDO! ---
    // Todas las dependencias de lifecycle ahora están unificadas a la versión 2.7.0
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")
    // NOTA: lifecycle-runtime-compose es una librería más nueva que a veces causa conflictos.
    // Con las dos líneas de arriba es suficiente para que viewModel() funcione. La eliminamos por seguridad.
    // implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.6") // <-- ELIMINADA

    // --- COMPOSE ---
    // El BOM (Bill of Materials) gestiona las versiones de las librerías de Compose
    implementation(platform("androidx.compose:compose-bom:2024.02.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")


    // --- NAVEGACIÓN ---
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // --- ÍCONOS ---
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")

    // --- OTRAS LIBRERÍAS ÚTILES ---
    implementation("io.coil-kt:coil-compose:2.5.0") // Para imágenes
    implementation("androidx.datastore:datastore-preferences:1.1.1") // Para guardar datos
    // Room (SQLite) - runtime y extensiones KTX
    implementation("androidx.room:room-runtime:2.6.1")    // <-- NUEVO
    implementation("androidx.room:room-ktx:2.6.1")        // <-- NUEVO

    ksp("androidx.room:room-compiler:2.6.1")// <-- NUEVO

    //manipular la carga de imagenes en el cache temporal
    implementation("io.coil-kt:coil-compose:2.7.0")
    

    // --- PRUEBAS (TESTING) ---
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Retrofit + Gson
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
// OkHttp (cliente HTTP)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
// Coroutines (si no las tienes aún)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

}
