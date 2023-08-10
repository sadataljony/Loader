package com.sadataljony.app.android.loader

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.sadataljony.app.android.loader.utils.ArcLoader
import com.sadataljony.app.android.loader.utils.SimpleArcDialog
import com.sadataljony.app.android.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var simpleArcDialog: SimpleArcDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        simpleArcDialog = ArcLoader(this).simpleArcLoader()
        simpleArcDialog.show()
        Handler().postDelayed({
            simpleArcDialog.dismiss()
        }, 5000)
    }
}