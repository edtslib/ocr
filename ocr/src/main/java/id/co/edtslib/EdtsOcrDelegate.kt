package id.co.edtslib

interface EdtsOcrDelegate {
    fun onCaptured(ktp: Any?, text: String?)
}