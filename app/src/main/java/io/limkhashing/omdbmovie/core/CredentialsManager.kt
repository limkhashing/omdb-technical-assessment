package io.limkhashing.omdbmovie.core

import io.limkhashing.omdbmovie.BuildConfig


object CredentialsManager {
    // TODO replace with your own API key with Environment Variable
    //  For testing, you can retrieve from README.md
    val OMDB_API_KEY: String
        get() = BuildConfig.OMDB_API_KEY

}