import org.gradle.api.JavaVersion

object Versions {

    val build_minSdk = 23
    val build_compileSdk = 29
    val build_targetSdk = 29
    val build_javaVersion = JavaVersion.VERSION_1_8
    val build_buildTools = "28.0.3"

    val androidXAnnotations = "1.1.0"
    val androidXLegacySupport = "1.0.0"

    val androidXAppCompat = "1.1.0-rc01"
    val androidXBiomertric = "1.0.0"
    val androidXRecyclerview = "1.0.0"
    val androidXNavigation = "2.2.0-alpha01"
    val androidXConstraintLayout = "1.1.3"
    val androidXMaterial = "1.1.0-alpha04"
    val androidXRoom = "2.2.2"
    val androidXLifecycle = "2.2.0"

    val junit = "4.12"
    val androidXTestEspresso = "3.1.0"
    val androidXTestCore = "1.2.0"
    val androidXTestExtKotlinRunner = "1.1.1"
    val androidXTestRules = "1.2.0"
    val androidXArchTest = "2.1.0"
    val androidXTestTruth = "1.0"


    val gradleandroid = "3.6.3"
    val kotlin = "1.3.61"
    val gradleversions = "0.21.0"

    val androidXCard = "1.0.0"

    val coroutines = "1.3.7"

    val mockito = "3.3.3"
    val dexMaker = "2.12.1"
    val mockk = "1.10.0"
    val kluent = "1.14"
    val truth = "1.0"

    val paper_db = "2.6"
    val paper_rx = "1.4.0"

    val robolectric = "4.3.1"

    val androidXFragment = "1.2.4"

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
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.androidXNavigation}"
}

object ApplicationDeps {
    // Kotlin
    val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    val kotlinCoroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    // Core
    val androidXAppCompat = "androidx.appcompat:appcompat:${Versions.androidXAppCompat}"
    val androidXConstraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.androidXConstraintLayout}"

    val androidXCard = "androidx.cardview:cardview:${Versions.androidXCard}"

    val androidXMaterial = "com.google.android.material:material:${Versions.androidXMaterial}"
    val androidXRecyclerview =
        "androidx.recyclerview:recyclerview:${Versions.androidXRecyclerview}"

    val androidXAnnotations = "androidx.annotation:annotation:${Versions.androidXAnnotations}"
    val androidXLegacySupport =
        "androidx.legacy:legacy-support-v4:${Versions.androidXLegacySupport}"

    // Android X
    val androidXBiomertric = "androidx.biometric:biometric:${Versions.androidXBiomertric}"
    val androidXRoom = "androidx.room:room-runtime:${Versions.androidXRoom}"
    val androidXRoomCompiler = "androidx.room:room-compiler:${Versions.androidXRoom}"

    // Navigation

    // KTX
    //val ktx_fragment = "androidx.fragment:fragment-ktx:$fragmentKtxVersion"
    val androidXRoomKtx = "androidx.room:room-ktx:${Versions.androidXRoom}"
    val lifecycleViewmodel =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.androidXLifecycle}"
    val lifecycle_runtime =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.androidXLifecycle}"
    val lifecycle_livedata =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.androidXLifecycle}"
}


object JvmUnitTestDeps {
    val junit = "junit:junit:${Versions.junit}"
    val mockitoCore = "org.mockito:mockito-core:${Versions.mockito}"
    val archCoreTest = "androidx.arch.core:core-testing:${Versions.androidXArchTest}"

    val kotlinCoroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    val kotlinCoroutinesTest =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
    val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"

    val espressoCore = "androidx.test.espresso:espresso-core:${Versions.androidXTestEspresso}"
    val espresssIdling =
        "androidx.test.espresso:espresso-idling-resource:${Versions.androidXTestEspresso}"
    val espressoIntents = "androidx.test.espresso:espresso-intents:${Versions.androidXTestEspresso}"
    val espressoContrib = "androidx.test.espresso:espresso-contrib:${Versions.androidXTestEspresso}"

    val truth = "com.google.truth:truth:${Versions.androidXTestTruth}"

    val mockk = "io.mockk:mockk:${Versions.mockk}"
    val kluent = "org.amshove.kluent:kluent:${Versions.kluent}"
}

object AndroidUnitTestDeps {
    val junit = "junit:junit:${Versions.junit}"
    val mockito = "org.mockito:mockito-core:${Versions.mockito}"
    val dexMaker = "com.linkedin.dexmaker:dexmaker-mockito:${Versions.dexMaker}"
    val kotlinCoroutinesTest =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
}

object AndroidTestDeps {
    val testCore = "androidx.test:core-ktx:${Versions.androidXTestCore}"
    val testJunitExt = "androidx.test.ext:junit-ktx:${Versions.androidXTestExtKotlinRunner}"
    val testRule = "androidx.test:rules:${Versions.androidXTestRules}"
    val roomTest = "androidx.room:room-testing:${Versions.androidXRoom}"
    val archCoreTest = "androidx.arch.core:core-testing:${Versions.androidXArchTest}"
    val espressoCore = "androidx.test.espresso:espresso-core:${Versions.androidXTestEspresso}"
    val espressoIntents = "androidx.test.espresso:espresso-intents:${Versions.androidXTestEspresso}"
    val espressoContrib = "androidx.test.espresso:espresso-contrib:${Versions.androidXTestEspresso}"
    val espressoIdlingConcurrent =
        "androidx.test.espresso.idling:idling-concurrent:${Versions.androidXTestEspresso}"
    val espressoIdlingResource =
        "androidx.test.espresso:espresso-idling-resource:${Versions.androidXTestEspresso}"
    val truth = "com.google.truth:truth:${Versions.androidXTestTruth}"
    val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    val robolectricAnnotation = "org.robolectric:annotations:${Versions.robolectric}"

    val fragment = "androidx.fragment:fragment:${Versions.androidXFragment}"
    val fragmentTest = "androidx.fragment:fragment-testing:${Versions.androidXFragment}"
    val testCorePlain = "androidx.test:core:${Versions.androidXTestCore}"


}


object DevelopmentDeps {
    val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"
    val leakCanaryNoop = "com.squareup.leakcanary:leakcanary-android-no-op:${Versions.leakCanary}"
}
