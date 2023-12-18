package com.example.project_v1.activity

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.AppCompatImageView
import com.example.project_v1.utils.PixelsUtils

class Balloon : AppCompatImageView, Animator.AnimatorListener,
    ValueAnimator.AnimatorUpdateListener, View.OnClickListener {
    constructor(context: Context) : super(context)

    private lateinit var animator: ValueAnimator
    private lateinit var listener: BalloonListener
    private var isPopped: Boolean = false
    lateinit var colorName: String
    var colorcode = 0
    private var score: Int = 0

    constructor(context: Context, imageResources: List<Int>, score: Int, rawHeight: Int) : super(context) {
        val randomImageResource = imageResources.random()
        setImageResource(randomImageResource)
        this.score = score

        val rawWidth = rawHeight / 2
        val dpHeight = PixelsUtils().pixelsToDp(height, context)
        val dpWidth = PixelsUtils().pixelsToDp(rawWidth, context)

        layoutParams = ViewGroup.LayoutParams(dpWidth, dpHeight)
        isClickable = true
        setOnClickListener(this)
        //setOnTouchListener(this)
        listener = context as BalloonListener

    }

    fun releaseBalloon(screenHeight: Int, duration: Int) {
        animator = ValueAnimator()
        animator.apply {
            setDuration(duration.toLong())
            setFloatValues(screenHeight.toFloat(), 0f)
            interpolator = LinearInterpolator()
            setTarget(this@Balloon)
            addListener(this@Balloon)
            addUpdateListener(this@Balloon)
            start()
        }

    }

    override fun onClick(v: View?) {
        if (!isPopped) {
            listener.popBalloon(this, true, this.colorcode,this.score);
            isPopped = true;
            animator.cancel();
        }

    }


    interface BalloonListener {
        abstract var score: Int

        fun popBalloon(balloon: Balloon?, touched: Boolean, color: Int, score: Int)
    }

    override fun onAnimationStart(animation: Animator) {
    }

    override fun onAnimationEnd(animation: Animator) {
        if (!isPopped) {
            listener.popBalloon(this, false, this.colorcode,this.score);
        }
    }

    override fun onAnimationCancel(animation: Animator) {
    }

    override fun onAnimationRepeat(animation: Animator) {
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        if (!isPopped) {
            y = animation.animatedValue as Float
        }
    }

    fun setPopped(popped: Boolean) {
        this.isPopped = popped
    }


}