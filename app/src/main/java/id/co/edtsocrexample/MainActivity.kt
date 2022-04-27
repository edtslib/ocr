package id.co.edtsocrexample

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import id.co.edtslib.ocr.EdtsOcr
import id.co.edtslib.ocr.EdtsOcrDelegate
import id.co.edtslib.ocr.EdtsOcrType
import id.co.edtslib.ocr.Ktp
import id.co.edtslib.util.PermissionUtil


class MainActivity : AppCompatActivity() {
    private lateinit var camera: CameraView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        camera = findViewById(R.id.camera)

        PermissionUtil.cameraPermission(this) {

        }

        findViewById<View>(R.id.button).setOnClickListener {
            camera.addCameraListener(object : CameraListener() {
                override fun onPictureTaken(result: PictureResult) {
                    result.toBitmap {
                        processOcr(it)
                    }
                }
            })
            camera.takePictureSnapshot()
        }
    }

    override fun onResume() {
        super.onResume()
        camera.open()
    }

    override fun onPause() {
        camera.close()
        super.onPause()
    }

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

}