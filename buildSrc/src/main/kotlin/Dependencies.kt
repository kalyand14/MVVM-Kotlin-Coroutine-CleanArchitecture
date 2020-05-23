import org.gradle.api.JavaVersion

object Versions {

    // <editor-fold desc="google">
    val androidx_appcompat = "1.1.0-rc01"
    val androidx_biomertric = "1.0.0"
    val androidx_core = "1.2.0-alpha01"
    val androidx_recyclerview = "1.0.0"
    val androidx_navigation = "2.2.0-alpha01"
    val androidx_constraintLayout = "1.1.3"
    val material = "1.1.0-alpha04"
    val support = "1.0.0"
    val room = "2.1.0"
    val androidx_lifecycle = "2.2.0"
    // </editor-fold>

    // <editor-fold desc="testing">
    val junit = "4.12"
    val androidx_espresso = "3.1.0"
    val androidx_testing = "1.1.1"
    //</editor-fold>

    // <editor-fold desc="tools">
    val gradleandroid = "3.6.3"
    val kotlin = "1.3.61"
    val gradleversions = "0.21.0"
    // </editor-fold>

    // <editor-fold desc="Other">
    val coroutines = "1.3.7"

    val paper_db = "2.6"
    val paper_rx = "1.4.0"

    val retrofit = "2.3.0"
    val retrofit_moshiConverter = "2.4.0"
    val retrofit_okhttp_logging_interceptor = "4.0.0"
    val retrofit_rxjavaAdapter = "2.2.0"

    val reactivex_android = "2.0.1"
    val reactivex_java = "2.1.3"
    val reactivex_kotlin = "2.3.0"

    val timber = "4.5.1"
    // </editor-fold>

}

object Deps {

    val build_minSdk = 23
    val build_compileSdk = 29
    val build_targetSdk = 29
    val build_javaVersion = JavaVersion.VERSION_1_8
    val build_buildTools = "28.0.3"

    //Android X'
    val androidx_appcompat = "androidx.appcompat:appcompat:${Versions.androidx_appcompat}"
    val androidx_biomertric = "androidx.biometric:biometric:${Versions.androidx_biomertric}"
    val androidx_core = "androidx.core:core-ktx:${Versions.androidx_core}"
    val androidx_constraintlayout =
        "androidx.constraintlayout:constraintlayout:${Versions.androidx_constraintLayout}"
    val androidx_cardview = "androidx.cardview:cardview${Versions.support}"
    val androidx_material = "com.google.android.material:material:${Versions.material}"

    val androidx_navigation_runtime =
        "androidx.navigation:navigation-runtime:${Versions.androidx_navigation}"
    val androidx_navigation_runtime_ktx =
        "androidx.navigation:navigation-runtime-ktx:${Versions.androidx_navigation}"
    val androidx_navigation_fragment =
        "androidx.navigation:navigation-fragment:${Versions.androidx_navigation}"
    val androidx_navigation_fragment_ktx =
        "androidx.navigation:navigation-fragment-ktx:${Versions.androidx_navigation}"
    val androidx_navigation_ui = "androidx.navigation:navigation-ui:${Versions.androidx_navigation}"
    val androidx_navigation_ui_ktx =
        "androidx.navigation:navigation-ui-ktx:${Versions.androidx_navigation}"
    val androidx_navigation_safe_args_plugin =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.androidx_navigation}"

    val androidx_recyclerview =
        "androidx.recyclerview:recyclerview:${Versions.androidx_recyclerview}"

    val androidx_lifecycle_viewmodel =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.androidx_lifecycle}"

    // Room and Lifecycle dependencies
    val room_runtime = "androidx.room:room-runtime:${Versions.room}"
    val room_kapt = "androidx.room:room-compiler:${Versions.room}"

    //Kotlin
    val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val kotlin_allopen = "org.jetbrains.kotlin:kotlin-allopen:${Versions.kotlin}"

    //Paper
    val paper_db = "io.paperdb:paperdb:${Versions.paper_db}"
    val paper_rx = "com.github.pakoito:RxPaper2:${Versions.paper_rx}"

    //Retrofit
    val retrofit_runtime = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val retrofit_gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    val retrofit_moshiConverter =
        "com.squareup.retrofit2:converter-moshi:${Versions.retrofit_moshiConverter}"
    val retrofit_mock = "com.squareup.retrofit2:retrofit-mock:${Versions.retrofit}"
    val retrofit_rxjavaAdapter =
        "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit_rxjavaAdapter}"
    val retrofit_loggingInterceptor =
        "com.squareup.okhttp3:logging-interceptor:${Versions.retrofit_okhttp_logging_interceptor}"

    //Rx
    val reactivex_java = "io.reactivex.rxjava2:rxjava:${Versions.reactivex_java}"
    val reactivex_android = "io.reactivex.rxjava2:rxandroid:${Versions.reactivex_android}"
    val reactivex_kotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.reactivex_kotlin}"

    //Coroutines
    val coroutine_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    val coroutine_android =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    //Timber
    val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    //Test
    val testlib_junit = "junit:junit:${Versions.junit}"

    //AndroidX Test
    val testandroidx_rules = "androidx.test:rules:${Versions.androidx_testing}"
    val testandroidx_runner = "androidx.test:runner:${Versions.androidx_testing}"
    val testandroidx_espressocore =
        "androidx.test.espresso:espresso-core:${Versions.androidx_espresso}"
    val testandroidx_espresso_idling =
        "androidx.test.espresso:espresso-idling-resource:${Versions.androidx_espresso}"


    val tools_gradleandroid = "com.android.tools.build:gradle:${Versions.gradleandroid}"
    val tools_kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    val tools_gradleversions =
        "com.github.ben-manes:gradle-versions-plugin:${Versions.gradleversions}"
    val tools_navigation_safe_args =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.androidx_navigation}"

}