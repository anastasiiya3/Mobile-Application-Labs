plugins { id("com.android.application"); id("org.jetbrains.kotlin.android"); id("com.google.gms.google-services") }
android { namespace = "com.example.lab6"; compileSdk = 35
 defaultConfig { applicationId = "com.example.lab6"; minSdk = 26; targetSdk = 35; versionCode = 1; versionName = "1.0" }
}
dependencies { implementation(platform("com.google.firebase:firebase-bom:33.7.0")); implementation("com.google.firebase:firebase-auth"); implementation("com.google.firebase:firebase-firestore"); implementation("com.squareup.picasso:picasso:2.8") }
