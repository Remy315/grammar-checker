package com.example.grammarchecker

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.core.content.ContextCompat
import android.graphics.drawable.GradientDrawable
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.graphics.Color

class AnimationHelper {

    companion object {
        
        fun animateCorrectAnswer(view: View) {
            // Create color animation from red to green
            val colorAnimation = ObjectAnimator.ofObject(
                view,
                "backgroundColor",
                ArgbEvaluator(),
                ContextCompat.getColor(view.context, R.color.error_red),
                ContextCompat.getColor(view.context, R.color.correct_green)
            )
            
            colorAnimation.duration = 1000
            colorAnimation.interpolator = AccelerateDecelerateInterpolator()
            colorAnimation.start()
        }
        
        fun createConfettiAnimation(container: ViewGroup) {
            // Create simple confetti effect with colored circles
            for (i in 0 until 8) {
                val confetti = ImageView(container.context)
                confetti.setBackgroundColor(getRandomColor())
                
                val size = 12
                val layoutParams = LinearLayout.LayoutParams(size, size)
                confetti.layoutParams = layoutParams
                
                container.addView(confetti)
                
                // Simple animation to make confetti fall
                val fallAnimation = ObjectAnimator.ofFloat(
                    confetti,
                    "translationY",
                    0f,
                    200f
                )
                fallAnimation.duration = 1500
                fallAnimation.startDelay = (i * 100).toLong()
                fallAnimation.start()
                
                val fadeAnimation = ObjectAnimator.ofFloat(
                    confetti,
                    "alpha",
                    1f,
                    0f
                )
                fadeAnimation.duration = 1500
                fadeAnimation.startDelay = (i * 100).toLong()
                fadeAnimation.start()
            }
        }
        
        fun animateTextCorrections(textView: TextView, originalText: String, correctedText: String) {
            // Animate text change with slide effect
            val fadeOut = ObjectAnimator.ofFloat(textView, "alpha", 1f, 0f)
            fadeOut.duration = 300
            
            val fadeIn = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f)
            fadeIn.duration = 300
            
            fadeOut.start()
            fadeOut.addListener {
                textView.text = correctedText
                fadeIn.start()
            }
        }
        
        fun createGreenSlideAnimation(view: View) {
            // Create a sliding green overlay effect
            val drawable = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(
                    Color.TRANSPARENT,
                    ContextCompat.getColor(view.context, R.color.correct_green)
                )
            )
            
            val overlayView = View(view.context)
            overlayView.background = drawable
            overlayView.alpha = 0.3f
            
            val layoutParams = ViewGroup.LayoutParams(
                view.width,
                view.height
            )
            
            if (view.parent is ViewGroup) {
                val parent = view.parent as ViewGroup
                val index = parent.indexOfChild(view)
                parent.addView(overlayView, index + 1, layoutParams)
                
                val slideAnimation = ObjectAnimator.ofFloat(
                    overlayView,
                    "translationX",
                    -view.width.toFloat(),
                    view.width.toFloat()
                )
                slideAnimation.duration = 800
                slideAnimation.start()
                
                // Remove overlay after animation
                slideAnimation.addListener {
                    parent.removeView(overlayView)
                }
            }
        }
        
        private fun getRandomColor(): Int {
            val colors = listOf(
                Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,
                Color.CYAN, Color.MAGENTA, Color.parseColor("#FF6B6B"),
                Color.parseColor("#4ECDC4"), Color.parseColor("#45B7D1"),
                Color.parseColor("#96CEB4"), Color.parseColor("#FECA57")
            )
            return colors.random()
        }
        
        fun showSuccessToast(view: View, message: String) {
            // Create a custom toast-like animation
            val overlay = view.rootView
            if (overlay is ViewGroup) {
                val toastView = TextView(view.context)
                toastView.text = message
                toastView.setBackgroundColor(
                    ContextCompat.getColor(view.context, R.color.correct_green)
                )
                toastView.setTextColor(Color.WHITE)
                toastView.setPadding(24, 16, 24, 16)
                toastView.textSize = 16f
                
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 50, 0, 0)
                
                overlay.addView(toastView, params)
                
                // Animate the toast
                val slideIn = ObjectAnimator.ofFloat(
                    toastView,
                    "translationY",
                    -100f,
                    0f
                )
                slideIn.duration = 300
                
                val fadeOut = ObjectAnimator.ofFloat(
                    toastView,
                    "alpha",
                    1f,
                    0f
                )
                fadeOut.duration = 500
                fadeOut.startDelay = 1500
                
                slideIn.start()
                
                fadeOut.addListener {
                    overlay.removeView(toastView)
                }
                fadeOut.start()
            }
        }
    }
}