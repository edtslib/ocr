package id.co.edtslib.ocr

interface EdtsOcrDelegate {
    fun onCaptured(captured: Any?, text: String?)
}