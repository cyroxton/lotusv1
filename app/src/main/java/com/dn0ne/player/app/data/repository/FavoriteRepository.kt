package com.dn0ne.player.app.data.repository

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteTrack : RealmObject {
    @PrimaryKey
    var uri: String = ""
}

interface FavoriteRepository {
    fun isFavorite(uri: String): Flow<Boolean>
    fun getFavoriteTracksUris(): Flow<List<String>>
    suspend fun addFavorite(uri: String)
    suspend fun removeFavorite(uri: String)
}

class RealmFavoriteRepository(private val realm: Realm) : FavoriteRepository {

    override fun isFavorite(uri: String): Flow<Boolean> {
        return realm.query<FavoriteTrack>("uri == $0", uri).asFlow()
            .map { it.list.isNotEmpty() }
    }

    override fun getFavoriteTracksUris(): Flow<List<String>> {
        return realm.query<FavoriteTrack>().asFlow().map { results ->
            results.list.map { it.uri }
        }
    }

    override suspend fun addFavorite(uri: String) {
        realm.write {
            copyToRealm(FavoriteTrack().apply { this.uri = uri })
        }
    }

    override suspend fun removeFavorite(uri: String) {
        realm.write {
            val favorite = query<FavoriteTrack>("uri == $0", uri).find().firstOrNull()
            favorite?.let { delete(it) }
        }
    }
}
