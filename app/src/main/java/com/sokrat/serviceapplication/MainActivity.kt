package com.sokrat.serviceapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val start = findViewById<Button>(R.id.start)
        val stop = findViewById<Button>(R.id.stop)


        val intent = Intent(this, MediaPlayerService::class.java)

        start.setOnClickListener {

            startForegroundService(intent)
        }

        stop.setOnClickListener {
            stopService(intent)
        }



    }
}