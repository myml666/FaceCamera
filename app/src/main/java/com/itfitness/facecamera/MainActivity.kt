package com.itfitness.facecamera

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceView
import kotlinx.android.synthetic.main.activity_main.*
import org.opencv.android.CameraBridgeViewBase
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity(),CameraBridgeViewBase.CvCameraViewListener2 {
    lateinit var faceDetector:CascadeClassifier
    override fun onCameraViewStarted(width: Int, height: Int) {

    }

    override fun onCameraViewStopped() {

    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
        var frame:Mat = inputFrame!!.rgba()
        Core.flip(frame, frame, 1)
        Core.rotate(frame,frame,Core.ROTATE_90_CLOCKWISE)
//        Imgproc.cvtColor(frame,frame,Imgproc.COLOR_BGR2GRAY)
//        val mean = Core.mean(frame).`val`[0]
//        Imgproc.threshold(frame,frame,mean, 255.0,Imgproc.THRESH_BINARY)
        haarFaceDetection(frame)
        return frame
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initCamera()
        initFaceDetector()
    }

    /**
     * 加载相机
     */
    private fun initCamera() {
        camera.visibility = SurfaceView.VISIBLE
        camera.setCvCameraViewListener(this)
        camera.setCameraIndex(1)//后置摄像头
        camera.enableView()

    }

    @Throws(IOException::class)
    private fun initFaceDetector() {
        val input = resources.openRawResource(R.raw.haarcascade_frontalface_alt)
        val cascadeDir = this.getDir("cascade", Context.MODE_PRIVATE)
        val file = File(cascadeDir.absoluteFile, "lbpcascade_frontalface.xml")
        val output = FileOutputStream(file)
        val buff = ByteArray(1024)
        var len = 0
        do {
            len = input.read(buff)
            if (len != -1) {
                output.write(buff, 0, len)
            }else{
                break
            }
        } while (true)
        input.close()
        output.close()
//        initLoad(file.getAbsolutePath())
        faceDetector = CascadeClassifier(file.getAbsolutePath())
        file.delete()
        cascadeDir.delete()
    }

    private fun haarFaceDetection(frame: Mat) {
        val gray = Mat()
        val faces = MatOfRect()
        Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGRA2GRAY)
        faceDetector.detectMultiScale(gray, faces, 1.1, 1, 0, Size(30.0, 30.0), Size(300.0, 300.0))
        val faceList = faces.toList()
        if (faceList.size > 0) {
            for (rect in faceList) {
                Imgproc.rectangle(frame, rect.tl(), rect.br(), Scalar(0.0, 0.0, 255.0), 2, 8, 0)
            }
        }
        gray.release()
        faces.release()
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
//    external fun faceDetection(nativeObjAddr: Long): String
//    external fun initLoad(absolutePath: String): String
    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
