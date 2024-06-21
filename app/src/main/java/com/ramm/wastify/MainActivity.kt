package com.ramm.wastify


import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.TextureView
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.ramm.wastify.databinding.ActivityMainBinding
import com.ramm.wastify.ml.WastifyMetadata
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp

class MainActivity : AppCompatActivity() {

    private lateinit var userBinding: ActivityMainBinding
    var colors = listOf<Int>(Color.BLUE, Color.GREEN)
    val paint = Paint()
    lateinit var labels:List<String>
    lateinit var imageProcessor: ImageProcessor
    lateinit var bitmap:Bitmap
    lateinit var imageView: ImageView
    lateinit var cameraDevice: CameraDevice
    lateinit var handler: Handler
    lateinit var cameraManager: CameraManager
    lateinit var textureView: TextureView
    lateinit var model: WastifyMetadata

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(userBinding.root)
        get_permission()

        labels = FileUtil.loadLabels(this, "labelmap.txt")
        imageProcessor = ImageProcessor.Builder().add(ResizeOp(300, 300, ResizeOp.ResizeMethod.BILINEAR)).build()
        model = WastifyMetadata.newInstance(this)
        val handlerThread = HandlerThread("videoThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)

        imageView = findViewById(R.id.imageView)

        textureView = findViewById(R.id.textureView)
        textureView.surfaceTextureListener = object:TextureView.SurfaceTextureListener{
            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                open_camera()
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {

            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
                bitmap = textureView.bitmap!!

                val image = TensorImage.fromBitmap(bitmap)

                val outputs = model.process(image)

                val location = outputs.locationAsTensorBuffer.floatArray;
                val category = outputs.categoryAsTensorBuffer.floatArray;
                val score = outputs.scoreAsTensorBuffer.floatArray;
                val numberOfDetections = outputs.numberOfDetectionsAsTensorBuffer.floatArray[0].toInt()

                var mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                val canvas = Canvas(mutable)

                val h = mutable.height
                val w = mutable.width
                paint.textSize = h/15f
                paint.strokeWidth = h/85f

                // Iterate through the detection results
                for (index in 0 until numberOfDetections) {
                    val score = score[index]
                    if (score > 0.5) { // Only consider detections with a confidence score above 0.5
                        // Get the bounding box location
                        val left = location[index * 4 + 1] * w
                        val top = location[index * 4] * h
                        val right = location[index * 4 + 3] * w
                        val bottom = location[index * 4 + 2] * h

                        // Get the detected class and corresponding color
                        val detectedClass = category[index].toInt()
                        val color = colors[detectedClass % colors.size]
                        val label = labels[detectedClass % labels.size]

                        // Drawing the bounding box
                        paint.style = Paint.Style.STROKE
                        paint.color = color
                        canvas.drawRect(RectF(left, top, right, bottom), paint)

                        // Drawing the label and score above the bounding box
                        paint.style = Paint.Style.FILL
                        paint.color = Color.YELLOW
                        val text = "$label: ${"%.2f".format(score)}"
                        canvas.drawText(text, left, top - 10, paint)
                    }
                }
                imageView.setImageBitmap(mutable)
            }

        }

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

    }

    @SuppressLint("MissingPermission")
    fun open_camera(){
        cameraManager.openCamera(cameraManager.cameraIdList[0], object:CameraDevice.StateCallback(){
            override fun onOpened(p0: CameraDevice) {
                cameraDevice = p0

                var surfaceTexture = textureView.surfaceTexture
                var surface = Surface(surfaceTexture)

                var captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                captureRequest.addTarget(surface)

                cameraDevice.createCaptureSession(listOf(surface), object: CameraCaptureSession.StateCallback(){
                    override fun onConfigured(session: CameraCaptureSession) {
                        session.setRepeatingRequest(captureRequest.build(), null, null)
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                    }
                }, handler)
            }

            override fun onDisconnected(p0: CameraDevice) {

            }

            override fun onError(p0: CameraDevice, p1: Int) {

            }
        }, handler)
    }

    fun get_permission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 101)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
            get_permission()
        }
    }
}