package il.co.galex.lambdas

import android.annotation.TargetApi
import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var audioManager: AudioManager

    private lateinit var onAudioFocusChange: (focusChange: Int) -> Unit
    //private lateinit var onAudioFocusChange: AudioManager.OnAudioFocusChangeListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        // works but behaves weirdly, can you find why?
        onAudioFocusChange = { focusChange: Int ->
            Log.d(TAG, "In onAudioFocusChange focus changed to = $focusChange")
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN -> TODO("resume playing")
                AudioManager.AUDIOFOCUS_LOSS -> TODO("abandon focus and stop playing")
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> TODO("pause but keep focus")
            }
        }

        // works perfectly fine
        /*onAudioFocusChange = object : AudioManager.OnAudioFocusChangeListener {
            override fun onAudioFocusChange(focusChange: Int) {
                Log.d(TAG, "In onAudioFocusChange (${this.toString().substringAfterLast("@")}), focus changed to = $focusChange")
                when (focusChange) {
                    AudioManager.AUDIOFOCUS_GAIN -> TODO("resume playing")
                    AudioManager.AUDIOFOCUS_LOSS -> TODO("abandon focus and stop playing")
                    AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> TODO("pause but keep focus")
                }
            }
        }*/

        obtain.setOnClickListener { requestAudioFocus() }
        release.setOnClickListener { abandonAudioFocus() }
    }

    private fun requestAudioFocus(): Boolean {

        Log.d(TAG, "requestAudioFocus() called")
        val focusRequest: Int = audioManager.requestAudioFocus(onAudioFocusChange,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN)

        Log.d(TAG, "granted = ${focusRequest == AudioManager.AUDIOFOCUS_REQUEST_GRANTED}")
        return focusRequest == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun abandonAudioFocus(): Boolean {
        Log.d(TAG, "abandonAudioFocus() called")
        val focusRequest: Int = audioManager.abandonAudioFocus(onAudioFocusChange)
        Log.d(TAG, "granted = ${focusRequest == AudioManager.AUDIOFOCUS_REQUEST_GRANTED}")
        return focusRequest == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
    }
}
