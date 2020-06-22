package com.example.vapulustest.utils

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.vapulustest.R

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.rotate() {
    this.startAnimation(
        AnimationUtils.loadAnimation(context, R.anim.amin_rotation)
            .apply { repeatCount = Animation.INFINITE })
}

fun View.stopRotate() {
    this.clearAnimation()
}