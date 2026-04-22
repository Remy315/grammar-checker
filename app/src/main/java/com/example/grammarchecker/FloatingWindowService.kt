package com.example.grammarchecker

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout

class FloatingWindowService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: LinearLayout
    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0f
    private var initialTouchY = 0f

    override fun onCreate() {
        super.onCreate()
        createFloatingWindow()
    }

    private fun createFloatingWindow() {
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val inflater = LayoutInflater.from(this)
        floatingView = inflater.inflate(R.layout.floating_window, null) as LinearLayout

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP or Gravity.START
        params.x = 0
        params.y = 100

        windowManager.addView(floatingView, params)

        setupFloatingView(params)
    }

    private fun setupFloatingView(params: WindowManager.LayoutParams) {
        val toggleButton = floatingView.findViewById<Button>(R.id.floatingToggle)
        val closeButton = floatingView.findViewById<Button>(R.id.floatingClose)

        // Make the floating window draggable
        floatingView.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = params.x
                    initialY = params.y
                    initialTouchX = motionEvent.rawX
                    initialTouchY = motionEvent.rawY
                    true
                }
                MotionEvent.ACTION_UP -> {
                    view.performClick()
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    params.x = initialX + (motionEvent.rawX - initialTouchX).toInt()
                    params.y = initialY + (motionEvent.rawY - initialTouchY).toInt()
                    windowManager.updateViewLayout(floatingView, params)
                    true
                }
                else -> false
            }
        }

        toggleButton.setOnClickListener {
            // Launch GrammarCheckActivity
            val intent = Intent(this, GrammarCheckActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        closeButton.setOnClickListener {
            stopSelf()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::floatingView.isInitialized) {
            windowManager.removeView(floatingView)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}