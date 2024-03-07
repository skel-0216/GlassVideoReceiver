package com.example.glassvideoreceiver

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.glassvideoreceiver.databinding.ActivityMainBinding
import com.example.testforstt.VoiceDetector

class MainActivity : AppCompatActivity(), GlassGestureDetector.OnGestureListener, VoiceDetector.OnVoiceListener{
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var voiceDetector: VoiceDetector
    private lateinit var gestureDetector: GlassGestureDetector
    private lateinit var textViewState : TextView
    private lateinit var textViewResult : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        textViewState = binding.textViewState
        textViewResult = binding.textViewResult

        voiceDetector = VoiceDetector(this, this)
        gestureDetector = GlassGestureDetector(this, this)
    }

    @SuppressLint("SetTextI18n")
    override fun onGesture(gesture: GlassGestureDetector.Gesture?): Boolean {
        when (gesture) {
            GlassGestureDetector.Gesture.TAP->{
                Log.d("DEBUG", "TAP")
                return true
            }
            GlassGestureDetector.Gesture.SWIPE_FORWARD -> {
                Log.d("DEBUG", "SWIPE_FORWARD")
//                voiceDetector.startListening()
                textViewState.text = "Swipe -> to stop"
                return true
            }
            GlassGestureDetector.Gesture.SWIPE_BACKWARD ->{
                Log.d("DEBUG", "SWIPE_BACKWARD")
//                voiceDetector.stopListening()
                textViewState.text = "Swipe <- to start"
                return true
            }

            GlassGestureDetector.Gesture.SWIPE_DOWN -> {
                finish()
                return true
            }
            else -> return false
        }
    }

    override fun onTextResultChanged(newText: String) {
        runOnUiThread {
            textViewResult.text = newText
        }
    }


}
