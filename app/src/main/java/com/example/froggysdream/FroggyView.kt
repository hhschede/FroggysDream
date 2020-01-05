package com.example.froggysdream


import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import android.view.SurfaceView
import android.util.Log
import android.view.MotionEvent


class FroggyView(context: Context, private val size: Point)
    : SurfaceView(context), Runnable {

    // This is our thread
    private val gameThread = Thread(this)

    // A boolean which we will set and unset
    private var playing = false
    // Game is paused at the start
    private var paused = true

    // A Canvas and a Paint object
    private var canvas: Canvas = Canvas()
    private val paint: Paint = Paint()

    // Platforms
    public var platforms: Platforms = Platforms(size.x, size.y)


    // The frog
    private var froggy: Froggy = Froggy(context, platforms, size.x, size.y)



    private var score = 0 // score
    private var waves = 1 // waves
    private var lives = 3 // number of lives
    private var highScore =  0 // high scores

    // How menacing should the sound be?
    private var menaceInterval: Long = 1000


    // Which menace sound should play next
    private var uhOrOh: Boolean = false
    // When did we last play a menacing sound
    private var lastMenaceTime = System.currentTimeMillis()

    private fun prepareLevel() {
        // Here we will initialize the game objects


    }


    override fun run() {
        // This variable tracks the game frame rate
        var fps: Long = 0

        while (playing) {

            // Capture the current time
            val startFrameTime = System.currentTimeMillis()

            // Update the frame
            if (!paused) {
                update(fps)
            }

            // Draw the frame
            draw()

            // Calculate the fps rate this frame
            val timeThisFrame = System.currentTimeMillis() - startFrameTime
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame
            }
        }
    }

    private fun update(fps: Long) {
        // Update the state of all the game objects

        // Move the frog
        froggy.update(fps, platforms)

    }


    private fun draw() {
        // Make sure our drawing surface is valid or the game will crash
        if (holder.surface.isValid) {
            // Lock the canvas ready to draw
            canvas = holder.lockCanvas()

            // Draw the background color
            canvas.drawColor(Color.argb(255, 0, 0, 0))

            // Choose the brush color for drawing
            paint.color = Color.argb(255, 0, 255, 0)

            // Draw all the game objects here
            canvas.drawRect(platforms.ground, paint) // ground


            // Froggy
            canvas.drawBitmap(froggy.bitmap, froggy.position.left,
                froggy.position.top
                , paint)

            // Draw the score and remaining lives
            // Change the brush color
            paint.color = Color.argb(255, 255, 255, 255)
            paint.textSize = 70f
            canvas.drawText("Score: $score   Lives: $lives Wave: " +
                    "$waves HI: $highScore", 20f, 75f, paint)

            // Draw everything to the screen
            holder.unlockCanvasAndPost(canvas)
        }
    }



    // If MainActivity is paused/stopped
    // shut down our thread.
    fun pause() {
        playing = false
        try {
            gameThread.join()
        } catch (e: InterruptedException) {
            Log.e("Error:", "joining thread")
        }
    }


    // If MainActivity is started then
    // start our thread.
    fun resume() {
        playing = true
        prepareLevel()
        gameThread.start()
    }

    // The SurfaceView class implements onTouchListener
    // So we can override this method and detect screen touches.
    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {

        var touchX = motionEvent.getX()
        var touchY = motionEvent.getY()
        var duration = motionEvent.eventTime - motionEvent.downTime

        // only register new event when the frog has stopped its last jump
        if (froggy.moving == Froggy.stopped){


            // Get the events to be recorded only when a button has been released
            when (motionEvent.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_POINTER_UP,
                MotionEvent.ACTION_UP-> {

                    paused = false
                    froggy.moving = Froggy.jumping

                    froggy.targetX = touchX
                    froggy.targetY = touchY
                    froggy.targetPower = duration


                }
        }


        }

        return true
    }
}