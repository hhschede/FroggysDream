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

    // Insects
    private val insects = ArrayList<Insects>()


    private var score = 0 // score
    private var bugsEaten = 0 // waves
    private var lives = 3 // number of lives
    private var highScore =  0 // high scores
    private var numInsects = 2


    private fun prepareLevel() {
        // Here we will initialize the insects

        for (i in 0 until numInsects){
            insects.add(Insects(context, size.x, size.y))
        }
    }

    private fun caughtInsect(frogPosition: RectF, insectList: ArrayList<Insects>){
        // if any of the corners of the frog box lie inside the box delineated by the insect,
        // the insect will die!

        insectList.forEachIndexed { i, insect ->
            val overlap = RectF.intersects(frogPosition, insect.position)
            if (overlap == true){
                insectList.removeAt(i) // remove the item

                // update scores and bugsEaten and determine if its a bad insect or a good one
                if (insect.badOrGood == 1) {
                    score += 10
                    bugsEaten += 1
                } else if (insect.badOrGood == 0){
                    score -= 10
                    lives -= 1
                }

            }
        }
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
        froggy.update(platforms, insects)

        // Update the insects
        for (insect_ in insects){
            insect_.update(4)
        }

        // frog eats stuff
        caughtInsect(froggy.position, insects)

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

            // draw platforms
            canvas.drawRect(platforms.ground, paint) // ground
            paint.color = Color.argb(255, 255, 255, 255)
            for (additionalPlatform in platforms.morePlatforms){
                canvas.drawRect(additionalPlatform, paint)
            }

            // Froggy
            canvas.drawBitmap(froggy.bitmap, froggy.position.left,
                froggy.position.top
                , paint)
            for (insect_ in insects){
                canvas.drawBitmap(insect_.bitmap1, insect_.position.left,
                    insect_.position.top, paint)
            }


            // Draw the score and remaining lives
            // Change the brush color

            paint.textSize = 70f
            canvas.drawText("Score: $score   Lives: $lives" +
                    " HI: $highScore", 20f, 75f, paint)

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