package com.example.privacy.Call

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telecom.Call
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.privacy.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_call.*
import java.util.concurrent.TimeUnit
import org.jetbrains.anko.audioManager

class CallActivity : AppCompatActivity() {

    private var isLoudspeaker = false
    private val disposables = CompositeDisposable()
    private lateinit var animation: AlphaAnimation
    private lateinit var number: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_call)
        number = intent.data.schemeSpecificPart
        // Setup image view animation
        animation = AlphaAnimation(1.toFloat(), 0.toFloat())
        animation.apply {
            duration = 500
            interpolator = LinearInterpolator()
            repeatCount = Animation.INFINITE
            repeatMode = Animation.REVERSE
        }
        fab_call_speakers.setImageDrawable(resources.getDrawable(R.drawable.ic_volume_off_white_24dp, applicationContext.theme))
        fab_call_speakers.setOnClickListener { speakerToggleClicked() }

    }

    override fun onStart() {
        super.onStart()

        answer.setOnClickListener {
            OngoingCall.answer()
        }

        hangup.setOnClickListener {
            OngoingCall.hangup()
        }

        OngoingCall.state
                .subscribe(::updateUi)
                .addTo(disposables)

        OngoingCall.state
                .filter { it == Call.STATE_DISCONNECTED }
                .delay(1, TimeUnit.SECONDS)
                .firstElement()
                .subscribe { finish() }
                .addTo(disposables)
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(state: Int) {
        if (number == "12345") {
            callInfo.text = "${state.asString().toLowerCase().capitalize()}\n" + ""
            answer.isVisible = state == Call.STATE_RINGING


            fab_call_speakers.isInvisible = state == Call.STATE_RINGING
            fab_call_speakers.isVisible = state == Call.STATE_DIALING



            hangup.isVisible = state in listOf(
                    Call.STATE_DIALING,
                    Call.STATE_RINGING,
                    Call.STATE_ACTIVE
            )
        } else {
            callInfo.text = "${state.asString().toLowerCase().capitalize()}\n$number"
            fab_call_speakers.isInvisible = state == Call.STATE_RINGING
            answer.isVisible = state == Call.STATE_RINGING

            hangup.isVisible = state in listOf(
                    Call.STATE_DIALING,
                    Call.STATE_RINGING,
                    Call.STATE_ACTIVE
            )
        }


    }
    private fun speakerToggleClicked() {
        when {
            isLoudspeaker -> {
                isLoudspeaker = false
                disableLoudSpeaker()
            }
            else -> {
                isLoudspeaker = true
                enableLoudSpeaker()
            }
        }
    }
    private fun enableLoudSpeaker() {
        audioManager.isSpeakerphoneOn = true
        fab_call_speakers.setImageDrawable(resources.getDrawable(R.drawable.ic_volume_up_white_24dp, applicationContext.theme))
    }

    private fun disableLoudSpeaker() {
        audioManager.isSpeakerphoneOn = false
        fab_call_speakers.setImageDrawable(resources.getDrawable(R.drawable.ic_volume_off_white_24dp, applicationContext.theme))
    }







    override fun onStop() {
        super.onStop()
        disposables.clear()
    }



    companion object {
        fun start(context: Context, call: Call) {
            Intent(context, CallActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .setData(call.details.handle)
                    .let(context::startActivity)
        }
    }
}
