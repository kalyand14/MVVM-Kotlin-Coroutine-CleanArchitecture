import org.gradle.api.JavaVersion

object Versions {

    val build_minSdk = 23
    val build_compileSdk = 29
    val build_targetSdk = 29
    val build_javaVersion = JavaVersion.VERSION_1_8
    val build_buildTools = "28.0.3"

    val androidx_appcompat = "1.1.0-rc01"
    val androidx_biomertric = "1.0.0"
    val androidx_core = "1.2.0-alpha01"
    val androidx_recyclerview = "1.0.0"
    val androidx_navigation = "2.2.0-alpha01"
    val androidx_constraintLayout = "1.1.3"
    val material = "1.1.0-alpha04"
    val androidx_support = "1.0.0"
    val androidx_room = "2.2.2"
    val androidx_lifecycle = "2.2.0"

    val junit = "4.12"
    val androidx_espresso = "3.1.0"
    val androidx_testing = "1.1.1"

    val gradleandroid = "3.6.3"
    val kotlin = "1.3.61"
    val gradleversions = "0.21.0"

    val card = "1.0.0"

    val coroutines = "1.3.7"

    val mockk = "1.10.0"
    val kluent = "1.14"

    val paper_db = "2.6"
    val paper_rx = "1.4.0"

    val robolectric = "4.3.1"


    val retrofit = "2.3.0"
    val retrofit_moshiConverter = "2.4.0"
    val retrofit_okhttp_logging_interceptor = "4.0.0"
    val retrofit_rxjavaAdapter = "2.2.0"

    val reactivex_android = "2.0.1"
    val reactivex_java = "2.1.3"
    val reactivex_kotlin = "2.3.0"

    val timber = "4.5.1"

    val leakCanary = "1.5"

}


object ToolsDeps {

    val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    val gradleandroid = "com.android.tools.build:gradle:${Versions.gradleandroid}"
    val gradleversions =
        "com.github.ben-manes:gradle-versions-plugin:${Versions.gradleversions}"
    val navigation_safe_args =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.androidx_navigation}"
}

object ApplicationDeps {
    // Kotlin
    val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    val kotlinCoroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    // Core
    val appCompat = "androidx.appcompat:appcompat:${Versions.androidx_appcompat}"
    val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.androidx_constraintLayout}"

    val cardView = "androidx.cardview:cardview:${Versions.card}"

    val material = "com.google.android.material:material:${Versions.material}"
    val recyclerView =
        "androidx.recyclerview:recyclerview:${Versions.androidx_recyclerview}"

    // Android X
    val biomertric = "androidx.biometric:biometric:${Versions.androidx_biomertric}"
    val roomRuntime = "androidx.room:room-runtime:${Versions.androidx_room}"
    val roomCompiler = "androidx.room:room-compiler:${Versions.androidx_room}"

    // Navigation

    // KTX
    //val ktx_fragment = "androidx.fragment:fragment-ktx:$fragmentKtxVersion"
    val lifecycleViewmodel =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.androidx_lifecycle}"
    val lifecycle_runtime =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.androidx_lifecycle}"
    val lifecycle_livedata =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.androidx_lifecycle}"
}


object UnitTestDeps {
    val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val kotlinTest = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
    val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    val junit = "junit:junit:${Versions.junit}"

    val mockk = "io.mockk:mockk:${Versions.mockk}"
    val kluent = "org.amshove.kluent:kluent:${Versions.kluent}"
}

object AcceptanceTestDeps {
    val testRunner = "androidx.test:runner:${Versions.androidx_testing}"
    val testRules = "androidx.test:rules:${Versions.androidx_testing}"
    val espressoCore = "androidx.test.espresso:espresso-core:${Versions.androidx_espresso}"
    val espresssIdling =
        "androidx.test.espresso:espresso-idling-resource:${Versions.androidx_espresso}"
    val espressoIntents = "androidx.test.espresso:espresso-intents:${Versions.androidx_espresso}"
    val espressoContrib = "androidx.test.espresso:espresso-contrib:${Versions.androidx_espresso}"
}

object DevelopmentDeps {
    val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"
    val leakCanaryNoop = "com.squareup.leakcanary:leakcanary-android-no-op:${Versions.leakCanary}"
}
