package com.example.froggysdream

import android.graphics.RectF


/** This class is responsible for providing the objects that Froggy can stand on
 * For now it is just the ground **/
class Platforms(screenX: Int, screenY: Int) {

    val platformThickness = 40f

    // provide the rectangle that represents the ground on start of the game
    val ground = RectF(0f,screenY.toFloat() - platformThickness, screenX.toFloat(), screenY.toFloat())


    // a function that is not yet random, but should be generating the platforms
    fun randomizeLocation(screenX: Int, screenY: Int): RectF {
        val widthPlatform = screenX.toFloat() / 3f
        val platformTop = screenY.toFloat() - 4f * platformThickness
        val platformLeft = screenX.toFloat() / 2f

        return RectF(platformLeft, platformTop,
            platformLeft + widthPlatform,
            platformTop - platformThickness)
    } // end randomizeLocation


    // list of the platforms that are not the ground
    val morePlatforms: List<RectF> = listOf(randomizeLocation(screenX, screenY))

}