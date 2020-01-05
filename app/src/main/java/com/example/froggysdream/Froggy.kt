package com.example.froggysdream

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.graphics.BitmapFactory
import android.util.Log
import kotlin.math.pow
import kotlin.math.sqrt

class Froggy(context: Context,
                 private val screenX: Int,
                 screenY: Int) {

    // The player frog will be represented by a Bitmap
    var bitmap: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.froggyicon)

    // How wide and high our frog will be
    val width = screenX / 10f
    private val height = screenY / 10f

    // This keeps track of where the frog is
    val position = RectF(
        screenX / 2f,
        screenY-height,
        screenX/2 + width,
        screenY.toFloat())

    // This will dictate how the duration of the press influences the distance the frog moves
    private val powerConstraint = 2

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
    var targetPower: Long = 0

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
    fun update(fps: Long) {
        // Move when frog is in jumping motion
        if (moving == jumping ) {
            //position.left -= speed / fps
            // move towards the position defined by targetX and targetY

            var deltaX = targetX - position.left
            var deltaY = targetY - position.top
            // rescale values so vector is unitary
            val vectorLength = sqrt(deltaX.pow(2) + deltaY.pow(2))
            deltaX /= vectorLength
            deltaY /= vectorLength

            // continue to change the coordinates as the duration of the press is still above 0
            if (targetPower > 0){
                position.left += deltaX
                position.top += deltaY
                targetPower -= powerConstraint
            }

            // indicate that the frog cannot move anymore
            if (targetPower <= 0) {
                moving = stopped
            }

            //position.left = targetX
            //position.top = targetY
        }

        else
        position.right = position.left + width
    }

}