package com.example.placesearch

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.crashlytics.android.Crashlytics
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.String


class MainActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {
    private val RECOVERY_DIALOG_REQUEST = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        youtube_view.initialize(Config.DEVELOPER_KEY, this)
        Crashlytics.getInstance().crash();

    }

    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        player: YouTubePlayer?,
        wasRestored: Boolean
    ) {
        if (!wasRestored) {

            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
            player!!.loadVideo(Config.YOUTUBE_VIDEO_CODE);
            // Hiding player controls
            player!!.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
            player!!.setShowFullscreenButton(true)
            player!!.fullscreenControlFlags = 1
        }

    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        errorReason: YouTubeInitializationResult?
    ) {
        if (errorReason!!.isUserRecoverableError()) {
            errorReason!!.getErrorDialog(this@MainActivity, RECOVERY_DIALOG_REQUEST).show()
        } else {
            val errorMessage = String.format(
                getString(R.string.error_player), errorReason.toString()
            )
            Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
}