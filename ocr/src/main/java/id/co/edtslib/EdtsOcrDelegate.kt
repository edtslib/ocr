package id.co.edtslib

interface EdtsOcrDelegate {
    fun onCaptured(captured: Any?, text: String?)
}