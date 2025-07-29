package com.dn0ne.player.app.presentation.components.playback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * ViewModel pour gérer les transformations de Flow liées aux paroles synchronisées.
 * Encapsule l'état de lecture et expose des transformations de Flow pour la position et l'état de lecture.
 */
class LyricsViewModel : ViewModel() {
    private val _playbackStateFlow = MutableStateFlow(PlaybackState())
    val playbackStateFlow = _playbackStateFlow.asStateFlow()
    
    // Transformations définies une seule fois dans le ViewModel
    val positionFlow = playbackStateFlow.map { it.position }
    val isPlayingFlow = playbackStateFlow.map { it.isPlaying }
    
    /**
     * Met à jour l'état de lecture interne avec un nouvel état.
     *
     * @param newState Le nouvel état de lecture à utiliser
     */
    fun updatePlaybackState(newState: PlaybackState) {
        _playbackStateFlow.value = newState
    }
    
    /**
     * Observe un flux d'état de lecture externe et met à jour l'état interne.
     * À utiliser dans un LaunchedEffect pour collecter le flux externe.
     *
     * @param externalPlaybackStateFlow Le flux d'état de lecture externe à observer
     */
    fun observeExternalPlaybackState(externalPlaybackStateFlow: StateFlow<PlaybackState>) {
        viewModelScope.launch {
            externalPlaybackStateFlow.collect { newState ->
                updatePlaybackState(newState)
            }
        }
    }
}