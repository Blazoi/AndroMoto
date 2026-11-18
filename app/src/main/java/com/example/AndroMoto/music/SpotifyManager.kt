package com.example.AndroMoto.music

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.AndroMoto.BuildConfig
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Image
import com.spotify.protocol.types.PlayerState

class SpotifyManager {

    private var appRemote: SpotifyAppRemote? = null

    var playerState by mutableStateOf<PlayerState?>(null)
        private set
    var isConnected by mutableStateOf(false)
        private set

    var albumArt by mutableStateOf<Bitmap?>(null)
        private set

    fun connect(context: Context) {
        if (appRemote?.isConnected == true) return

        val params = ConnectionParams.Builder(BuildConfig.SPOTIFY_CLIENT_ID)
            .setRedirectUri("andromoto://callback")
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(context, params, object : Connector.ConnectionListener {
            override fun onConnected(remote: SpotifyAppRemote) {
                appRemote = remote
                isConnected = true
                remote.playerApi.subscribeToPlayerState()
                    .setEventCallback {
                        playerState = it
                        loadAlbumArt(it)
                    }
            }

            override fun onFailure(error: Throwable) {
                isConnected = false
                Log.e("SPOTIFY", "Connection failed", error)
            }
        })
    }

    fun disconnect() {
        appRemote?.let { SpotifyAppRemote.disconnect(it) }
        appRemote = null
        isConnected = false
        albumArt = null
    }

    fun playPause() {
        val api = appRemote?.playerApi ?: return
        if (playerState?.isPaused == true) api.resume() else api.pause()
    }

    fun next() { appRemote?.playerApi?.skipNext() }
    fun previous() { appRemote?.playerApi?.skipPrevious() }
    fun play(uri: String) { appRemote?.playerApi?.play(uri) }

    fun loadAlbumArt(state: PlayerState) {
        val track = state.track ?: return
//        if (track.uri == lastTrackUri) return
//        lastTrackUri = track.uri

        appRemote?.imagesApi
            ?.getImage(track.imageUri, Image.Dimension.LARGE)
            ?.setResultCallback { bitmap -> albumArt = bitmap }
            ?.setErrorCallback { Log.e("SPOTIFY", "Cover load failed", it) }
    }
}