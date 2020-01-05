package com.example.froggysdream

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.graphics.Point

class MainActivity : Activity() {

    private var FroggyView: FroggyView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val display = windowManager.defaultDisplay // retrieve screen details
        val size = Point() // load resolution
        display.getSize(size)

        // Initialize gameView and set it
        FroggyView = FroggyView(this, size)
        setContentView(FroggyView)
    }

    // This method executes when the player starts the game
    override fun onResume() {
        super.onResume()

        // Tell the gameView resume method to execute
        FroggyView?.resume()
    }

    // When player quits the game
    override fun onPause() {
        super.onPause()

        // Pause method execute
        FroggyView?.pause()
    }
}
