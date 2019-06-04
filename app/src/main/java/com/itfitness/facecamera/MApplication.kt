package com.itfitness.facecamera

import android.app.Application
import android.widget.Toast
import org.opencv.android.OpenCVLoader

/**
 *
 * @ProjectName:    FaceCamera
 * @Package:        com.itfitness.facecamera
 * @ClassName:      MApplication
 * @Description:     java类作用描述 ：
 * @Author:         作者名：lml
 * @CreateDate:     2019/6/4 10:00
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/6/4 10:00
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class MApplication:Application(){
    override fun onCreate() {
        super.onCreate()
        val initDebug = OpenCVLoader.initDebug()
        if(initDebug){
            Toast.makeText(this,"加载成功", Toast.LENGTH_SHORT).show()
        }
    }
}