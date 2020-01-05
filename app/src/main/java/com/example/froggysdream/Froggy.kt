package com.example.froggysdream

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.graphics.BitmapFactory
import android.util.Log
import kotlin.math.pow
import kotlin.math.sqrt

class Froggy(context: Context,
             platforms: Platforms,
             private val screenX: Int,
             screenY: Int) {

    // Bitmap representation of frog
    var bitmap: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.froggyicon)

    // How wide and high our frog will be
    val width = screenX / 10f
    private val height = screenY / 10f

    // This keeps track of where the frog is
    val position = RectF(
        screenX / 2f,
        platforms.ground.top-height,
        screenX/2 + width,
        screenY.toFloat())

    // This will dictate how the duration of the press influences the distance the frog moves
    private val powerConstraint = 2
    private var gravity = 0.5f

    // This data is accessible using ClassName.propertyName
    companion object {
        // Which ways can the frog move
        const val stopped = 0
        const val jumping = 1
    }

    // Is the frog moving and in which direction
    // Start off stopped
    var moving = stopped
    var targetX = 0f
    var targetY = 0f
    var time = 0f
    var targetPower: Long = 0
    var peakHeight = 0f
    var falling = false

    init{
        // stretch the bitmap to a size
        // appropriate for the screen resolution
        bitmap = Bitmap.createScaledBitmap(bitmap,
            width.toInt() ,
            height.toInt() ,
            false)
    }

    // This update method will be called from update in
    // FroggyView. It determines if the player's
    // frog needs to move and changes the coordinates
    fun update(platforms: Platforms) {
        // Move when frog is in jumping motion
        if (moving == jumping) {
            //position.left -= speed / fps
            // move towards the position defined by targetX and targetY

            var deltaX = targetX - position.left
            var deltaY = targetY - position.top
            // rescale values so vector is unitary
            val vectorLength = sqrt(deltaX.pow(2) + deltaY.pow(2)) /
                    sqrt(targetPower.toDouble() / 2.toDouble()).toFloat()//8f
            deltaX /= vectorLength
            deltaY /= vectorLength


            // continue to change the coordinates as the duration of the press is still above 0
            if (targetPower > 0){
                position.left += deltaX
                position.top += deltaY + gravity * (time.pow(2) / 100)

                // record froggy's peakHeight
                if (position.top > peakHeight){
                    peakHeight = position.top
                }
                if (peakHeight > position.top){
                    falling = true
                }

                targetPower -= powerConstraint
            }

            // if the frog has started falling, find the next platform that intercepts the fall
            if (falling == true){
                if (time > 10 && findInterceptingPlatform(position, platforms)) {
                    moving = stopped
                    gravity = 0.5f
                    time = 0f
                }
            }

            if (position.top + height < platforms.ground.top){
                position.top += gravity * (time.pow(2) / 1000)
                //gravity += 0.1f
            }


            time += 1f // increase time so you fall faster


            // indicate that the frog cannot move anymore either because it landed on a platform
            // or because the duration is over
            if (time > 10 && withinPlatformBounds(platforms.ground.top, position.top + height)) {
                moving = stopped
                gravity = 0.5f // reset gravity
                time = 0f // reset time
            }

        }

        else
        position.right = position.left + width
    }

    // function to handle stupid float problems so that the frog can be considered 'stopped'
    private fun withinPlatformBounds(platformTop: Float, frogPosition: Float) : Boolean{
        var inBounds = false
        if (platformTop - 1f < frogPosition || platformTop + 1f < frogPosition) {
            inBounds = true
        }
        return inBounds
    }

    // function to determine which platform the frog should land on
    private fun findInterceptingPlatform(frogPosition: RectF,
                                    platforms: Platforms) : Boolean{

        var interception = false
        for (platform_ in platforms.morePlatforms){

            // determine if a platform meets the requirements of intercepting the frog's fall
            if (withinPlatformBounds(platform_.top, frogPosition.top + height)
                && platform_.left <= frogPosition.left
                && platform_.right >= frogPosition.right){
                interception = true
            }

        }
        return interception
    }


}