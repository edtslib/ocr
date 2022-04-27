# Edts OCR

![Edts OCR](https://i.ibb.co/vDskvs1/Screen-Shot-2022-04-27-at-15-03-50.png)
## Setup
### Gradle

Add this to your project level `build.gradle`:
```groovy
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
Add this to your app `build.gradle`:
```groovy
dependencies {
    implementation 'com.github.edtslib:ocr:latest'
}
```
# Example

```kotlin
    private fun processOcr(bitmap: Bitmap?) {
        val ocr = EdtsOcr(EdtsOcrType.KTP, object : EdtsOcrDelegate {
            @SuppressLint("SetTextI18n")
            override fun onCaptured(captured: Any?, text: String?) {
                val tvNik = findViewById<TextView>(R.id.tvNik)
                if (captured is Ktp) {
                    tvNik.text = "NIK: ${captured.nik}"
                }
                else {
                    tvNik.text = "Nik tidak dikenali"
                }
            }
        })
        ocr.process(bitmap)
    }
```

