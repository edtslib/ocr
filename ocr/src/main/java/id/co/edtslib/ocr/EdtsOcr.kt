package id.co.edtslib.ocr

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class EdtsOcr(private val type: EdtsOcrType,
              private val delegate: EdtsOcrDelegate
) {
    fun process(bitmap: Bitmap?) {
        if (bitmap == null) {
            delegate.onCaptured(null, null)
        }
        else {
            start(bitmap)
        }
    }

    fun process(file: String?) {
        if (file == null) {
            delegate.onCaptured(null, null)

        }
        else {
            val bitmap = BitmapFactory.decodeFile(file)
            process(bitmap)
        }
    }

    private fun start(bitmap: Bitmap) {
        when(type) {
            EdtsOcrType.KTP -> processKTP(bitmap)
        }
    }

    private fun processKTP(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer =
            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val s = visionText.text
                val nik = getNik(s)
                if (nik?.isNotEmpty() == true) {
                    delegate.onCaptured(Ktp(nik), s)
                } else {
                    delegate.onCaptured(null, s)
                }
            }
            .addOnFailureListener {

                delegate.onCaptured(null, null)
            }
    }

    private fun getNik(s: String): String? {
        for (i in s.indices) {
            val cc = s[i]
            if ((cc in '0'..'9') || (cc in 'a'..'z') || (cc in 'A'..'Z')) {
                if (i+16 > s.length) return null

                var candidate = s.substring(i, i+16)
                val nNumeric = numericCount(candidate)
                if (nNumeric == 16) {
                    return candidate
                }

                if (nNumeric >= 10) {
                    val temp = candidate.replace(" ", "")
                    val space = candidate.length - temp.length
                    if (space > 0) {
                        if (i+16+space > s.length) return null

                        candidate = s.substring(i, i+16+space).replace(" ", "")
                    }

                    val p = prediction(candidate)
                    if (numericCount(p) >= 16) {
                        return p
                    }
                }
            }
        }

        return null
    }

    private fun numericCount(s: String): Int {
        var count = 0
        for (item in s) {
            if (item in '0'..'9') {
                count++
            }
        }

        return count
    }

    private fun prediction(s: String): String {
        var p = s.replace("b", "6")
        p = p.replace("l", "1")
        p = p.replace("L", "6")
        p = p.replace("i", "1", ignoreCase = true)
        p = p.replace("h", "1")
        p = p.replace("o", "0", ignoreCase = true)
        p = p.replace("D", "0")
        p = p.replace("s", "5", ignoreCase = true)

        return p
    }
}