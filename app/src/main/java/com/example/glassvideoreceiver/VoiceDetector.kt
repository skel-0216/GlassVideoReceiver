package com.example.testforstt

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat



class VoiceDetector(private val activity: ComponentActivity, private val onVoiceListener: OnVoiceListener) {
    private var mRecognizer: SpeechRecognizer? = null

    private var isListening = false
    private var currentLanguage: String = "ko-KR" // "en-US"

    init {
        getPermission()
        initializeRecognizer()
    }

    interface OnVoiceListener {
        fun onTextResultChanged(newText: String)
    }

    private fun initializeRecognizer() {
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(activity)
        mRecognizer?.setRecognitionListener(getRecognitionListener())
    }

    private fun getRecognitionListener(): RecognitionListener {
        return object : RecognitionListener {

            // 말하기 시작할 준비가되면 호출
            override fun onReadyForSpeech(params: Bundle?) {
            }
            // 말하기 시작했을 때 호출
            override fun onBeginningOfSpeech() {
            }
            // 입력받는 소리의 크기를 알려줌
            override fun onRmsChanged(dB: Float) {
            }
            // 말을 시작하고 인식이 된 단어를 buffer에 담음
            override fun onBufferReceived(p0: ByteArray?) {
            }
            // 말하기가 끝났을 때
            override fun onEndOfSpeech() {
            }
            // 에러 발생
            override fun onError(error: Int) {
                mRecognizer?.cancel()
                mRecognizer?.startListening(
                    Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).putExtra(
                        RecognizerIntent.EXTRA_CALLING_PACKAGE,
                        activity.packageName
                    ).putExtra(RecognizerIntent.EXTRA_LANGUAGE, currentLanguage)
                )
            }

            // 인식 결과가 준비되면 호출
            override fun onResults(results: Bundle?) {
                val matches = results!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    val recognizedText = matches[0]
                    Log.d("===DEBUG===", "onResult recognizedText : $recognizedText")
                    // 음성으로 인식된 텍스트 전달
                    onVoiceListener.onTextResultChanged(recognizedText)

                    // 음성인식 재시작
                    Log.d("===DEBUG===", "onResult isListening : $isListening")
                    if(isListening) { startListening() }
                }

            }
            // 부분 인식 결과를 사용할 수 있을 때 호출
            override fun onPartialResults(p0: Bundle?) {
            }
            // 향후 이벤트를 추가하기 위해 예약
            override fun onEvent(p0: Int, p1: Bundle?) {
            }
        }
    }

    fun startListening() {
        isListening = true
        Log.d("===DEBUG===", "startListening() called")
        mRecognizer?.startListening(
            Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).putExtra(
                RecognizerIntent.EXTRA_CALLING_PACKAGE,
                activity.packageName
            ).putExtra(RecognizerIntent.EXTRA_LANGUAGE, currentLanguage)
        )
    }

    fun stopListening() {
        Log.d("===DEBUG===", "stopListening() called")
        isListening = false
    }

    fun changeLanguage(newLanguage: String) {
        currentLanguage = newLanguage
        stopListening()
        startListening()
    }

    fun getListeningState(): Boolean {
        return isListening
    }

    private fun getPermission(){
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없는 경우 권한 요청
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                123
            )
        }
    }
}
