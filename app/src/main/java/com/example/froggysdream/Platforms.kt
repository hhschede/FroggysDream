package com.example.froggysdream

import android.graphics.RectF


/** This class is responsible for providing the objects that Froggy can stand on
 * For now it is just the ground **/
class Platforms(screenX: Int, screenY: Int) {

    // provide the rectangle that represents the ground on start of the game
    val ground = RectF(0f,screenY.toFloat() - 40f, screenX.toFloat(), screenY.toFloat())

}