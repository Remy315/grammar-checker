package com.example.grammarchecker

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var startButton: Button
    private lateinit var statusText: TextView
    
    companion object {
        private const val REQUEST_OVERLAY_PERMISSION = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupClickListeners()
        checkOverlayPermission()
    }

    private fun initializeViews() {
        startButton = findViewById(R.id.startButton)
        statusText = findViewById(R.id.statusText)
    }

    private fun setupClickListeners() {
        startButton.setOnClickListener {
            if (hasOverlayPermission()) {
                startFloatingWindow()
            } else {
                requestOverlayPermission()
            }
        }
    }

    private fun checkOverlayPermission() {
        if (hasOverlayPermission()) {
            statusText.text = "Ready to start"
            startButton.text = "Start Grammar Checker"
        } else {
            statusText.text = "Please grant overlay permission"
            startButton.text = "Grant Permission"
        }
    }

    private fun hasOverlayPermission(): Boolean {
        return Settings.canDrawOverlays(this)
    }

    private fun requestOverlayPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION)
    }

    private fun startFloatingWindow() {
        val serviceIntent = Intent(this, FloatingWindowService::class.java)
        startService(serviceIntent)
        
        startButton.text = "Stop Grammar Checker"
        statusText.text = "Floating window active"
        
        Toast.makeText(this, "Grammar checker started! Look for the floating window.", Toast.LENGTH_LONG).show()
        
        // Launch the grammar check activity
        val checkIntent = Intent(this, GrammarCheckActivity::class.java)
        startActivity(checkIntent)
    }

    private fun stopFloatingWindow() {
        val serviceIntent = Intent(this, FloatingWindowService::class.java)
        stopService(serviceIntent)
        
        startButton.text = "Start Grammar Checker"
        statusText.text = "Grammar checker stopped"
        
        Toast.makeText(this, "Grammar checker stopped", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            if (hasOverlayPermission()) {
                checkOverlayPermission()
                Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission required to continue", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkOverlayPermission()
    }
}