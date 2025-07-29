package com.dn0ne.player.app.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dn0ne.player.app.presentation.components.playback.PlaybackState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

/**
 * ViewModel pour la gestion des paroles synchronisées.
 * 
 * Ce ViewModel encapsule la logique de transformation des Flows liés à la lecture
 * et aux paroles, évitant ainsi les transformations directes dans les composables.
 */
class LyricsViewModel : ViewModel() {
    
    // Flow d'état de lecture interne (à initialiser via injection ou setter)
    private val _playbackStateFlow = MutableStateFlow<PlaybackState?>(null)
    
    // Flow d'état de lecture exposé publiquement
    val playbackStateFlow: StateFlow<PlaybackState?> = _playbackStateFlow.asStateFlow()
    
    // Transformation du Flow pour obtenir uniquement la position
    val positionFlow: Flow<Long> = playbackStateFlow.map { it?.position ?: 0L }
    
    // Transformation du Flow pour obtenir l'état de lecture
    val isPlayingFlow: Flow<Boolean> = playbackStateFlow.map { it?.isPlaying ?: false }
    
    /**
     * Met à jour l'état de lecture.
     *
     * @param playbackState Le nouvel état de lecture
     */
    fun updatePlaybackState(playbackState: PlaybackState) {
        _playbackStateFlow.value = playbackState
    }
    
    /**
     * Met à jour l'état de lecture à partir d'un Flow externe.
     *
     * @param externalPlaybackStateFlow Flow externe d'état de lecture
     */
    fun observeExternalPlaybackState(externalPlaybackStateFlow: StateFlow<PlaybackState>) {
        // Dans un cas réel, on utiliserait viewModelScope.launch pour collecter le Flow externe
        // et mettre à jour le Flow interne
    }
}