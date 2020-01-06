package com.example.froggysdream

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import java.util.*
import android.graphics.BitmapFactory
import java.util.Random

class Insects(context: Context, screenX: Int, screenY: Int) {
    // How wide, high and spaced out are the invader will be
    var width = screenX / 7f

    val topCoord = (150..screenY - (screenY.toFloat() / 5f).toInt()).random()
    val leftCoord = (150..screenX - 150).random()

    var position = RectF(
        leftCoord.toFloat(),
        topCoord.toFloat(),
        leftCoord + width,
        topCoord + width
    )

    // This will hold the pixels per second speed that the insect will move
    private var speed = 40f
    private val left = 1
    private val right = 2

    private var moving = if ((0..1).random() == 0) right else left // randomize start direction


    // Initialize the bitmaps
    var badOrGood = (0..1).random() // 0 is a butterfly, 1 is a wasp
    var bitmap1 = if (badOrGood == 0) {
        BitmapFactory.decodeResource(
            context.resources,
            R.drawable.wasp)
    } else {
        BitmapFactory.decodeResource(
            context.resources,
            R.drawable.butterfly)
    }

    init{
        // stretch the bitmap to a size
        // appropriate for the screen resolution
        bitmap1 = Bitmap.createScaledBitmap(bitmap1,
            width.toInt() ,
            width.toInt() ,
            false)
    }


    fun update(speed: Int) {
        // move the insects left and right a bit
        if (moving == right){
            position.left += speed
        } else if (moving == left){
            position.left -= speed
        }
        // check if it is time to make the insect turn the other direction
        if (position.left < leftCoord - 60){
            moving = right
        } else if (position.left > leftCoord + 60){
            moving = left
        }

        // also check that if the frog eats them they disappear
    }

}