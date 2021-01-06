package com.example.opencv

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_open_cv.*

class OpenCVActivity : AppCompatActivity() {
    companion object {
        init {
            System.loadLibrary("openCV")
        }
    }

    private val bitmap:Bitmap by lazy {
        BitmapFactory.decodeResource(resources,R.drawable.github_avtor)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_cv)
        openCV.setOnClickListener {
            image_right.setImageBitmap(opBitmap(bitmap,Bitmap.Config.ARGB_8888))
        }
    }

    private external fun opBitmap(bitmap: Bitmap,config: Bitmap.Config): Bitmap
}