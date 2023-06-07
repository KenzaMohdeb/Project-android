package com.uqam.mentallys.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.uqam.mentallys.data.responses.CurrentUser
import com.uqam.mentallys.data.responses.LoginUserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mentally_dataStore_cache")

class UserPreferences @Inject constructor(context: Context) {

    private val appContext = context.applicationContext

    val accessToken: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN]
        }

    val getAccessToken: String
        get() = appContext.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN]
        }.toString()

    val userID: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[ID]
        }
    fun getUserInfo() = appContext.dataStore.data.map { preferences ->

        LoginUserInfo(
            firstName = preferences[FIRSTNAME],
            lastName = preferences[LASTNAME],
            fullName = preferences[FULLNAME],
            email = preferences[EMAIL],
            id = preferences[ID],
            image = preferences[IMAGE],
            CommunicationUserId=preferences[AZURE_USER_ID],
            AccessToken = preferences[AZURE_USER_TOKEN],
            AccessTokenExpiresOn = preferences[AZURE_TOKEN_EXPIRE_DATE]
        )
    }
    suspend fun saveCurrentUser(token: String, currentUser: CurrentUser) {
        appContext.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = token
            preferences[FIRSTNAME] = currentUser.firstname
            preferences[LASTNAME] = currentUser.lastname
            preferences[FULLNAME] = currentUser.fullName
            preferences[ID] = currentUser.id
            preferences[EMAIL] = currentUser.email
            preferences[IMAGE] = currentUser.image
            preferences[AZURE_USER_ID] = currentUser.communicationUserId
            preferences[AZURE_USER_TOKEN] = currentUser.accessToken
            preferences[AZURE_TOKEN_EXPIRE_DATE] = currentUser.accessTokenExpiresOn
        }
    }

    suspend fun saveAccessTokens(accessToken: String, refreshToken: String) {
        appContext.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun saveAzureInfo(communicationUserId: String, accessToken: String, accessTokenExpiresOn:String ) {
        appContext.dataStore.edit { preferences ->
            preferences[AZURE_USER_ID] = communicationUserId
            preferences[AZURE_USER_TOKEN] = accessToken
            preferences[AZURE_TOKEN_EXPIRE_DATE] = accessTokenExpiresOn
        }
    }
    suspend fun clear() {
        appContext.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("key_access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("key_refresh_token")
        private val FIRSTNAME = stringPreferencesKey("key_firstName")
        private val LASTNAME = stringPreferencesKey("key_lastName")
        private val FULLNAME = stringPreferencesKey("key_fullName")
        private val ID = stringPreferencesKey("key_id")
        private val EMAIL = stringPreferencesKey("key_email")
        private val IMAGE = stringPreferencesKey("key_image")
        private val AZURE_USER_ID = stringPreferencesKey("key_azure_User_Id")
        private val AZURE_USER_TOKEN = stringPreferencesKey("key_azure_user_token")
        private val AZURE_TOKEN_EXPIRE_DATE  = stringPreferencesKey("key_azure_token_expireDate")

    }

}