package com.dn0ne.player.app.data.repository

import com.dn0ne.player.app.domain.track.Track

import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun getTracks(): Flow<List<Track>>
    fun getFoldersWithAudio(): Set<String>
    fun invalidateCache()
}