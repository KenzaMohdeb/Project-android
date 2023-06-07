package com.uqam.mentallys.di

import android.content.Context
import androidx.room.Room
import com.uqam.mentallys.data.UserPreferences
import com.uqam.mentallys.data.dao.EventDatabaseDao
import com.uqam.mentallys.data.dao.ResourceDatabaseDao
import com.uqam.mentallys.data.database.MentallysDatabase
import com.uqam.mentallys.data.datasources.*
import com.uqam.mentallys.network.AuthApi
import com.uqam.mentallys.network.RemoteDataSource
import com.uqam.mentallys.network.UserApi
import com.uqam.mentallys.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideRemoteDataSource(): RemoteDataSource {
        return RemoteDataSource()
    }

    @Singleton
    @Provides
    fun provideAuthApi(
        remoteDataSource: RemoteDataSource,
        @ApplicationContext context: Context
    ): AuthApi {
        return remoteDataSource.buildApi(AuthApi::class.java,context)
    }

    @Singleton
    @Provides
    fun provideUserApi(
        remoteDataSource: RemoteDataSource,
        @ApplicationContext context: Context
    ): UserApi {
        return remoteDataSource.buildApi(UserApi::class.java, context)
    }

    @Singleton
    @Provides
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }

    @Singleton
    @Provides
    @Named("auth_token")
    fun provideAuthToken(userPreferences: UserPreferences): String {
        return userPreferences.getAccessToken

    }

    @Provides
    fun provideUserRepository(
        userApi: UserApi
    ): UserRepository {
        return UserRepository(userApi)
    }

    @Singleton
    @Provides
    fun provideEventDao(mentallysDatabase: MentallysDatabase): EventDatabaseDao =
        mentallysDatabase.eventDatabaseDao()

    @Singleton
    @Provides
    fun resourceHistoryDao(mentallysDatabase: MentallysDatabase): ResourceDatabaseDao =
        mentallysDatabase.resourceHistoryDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): MentallysDatabase =
        Room.databaseBuilder(
            context,
            MentallysDatabase::class.java,
            "mentallys_db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideEventRepository(
        dao: EventDatabaseDao,
        eventTypes: EventTypeDataSource,
        eventContexts: ContextDataSource,
        eventModalityDataSource: ModalityDataSource,
        eventTagDataSource: TagDataSource,
    ) = EventRepository(
        dao,
        eventTypes,
        eventContexts,
        eventModalityDataSource,
        eventTagDataSource
    ) as EventRepositoryInterface

    @Singleton
    @Provides
    fun provideHomePageRepository(
        homePageMedicList: MedicDataSource,
        homePageSupportList: SupportDataSource,
        homePagePrivateList: PrivateDataSource,
        homePageSuggestionList: SuggestionDataSource
    ) = HomePageRepository(
        homePageMedicList,
        homePageSupportList,
        homePagePrivateList,
        homePageSuggestionList
    ) as HomePageRepositoryInterface

    @Singleton
    @Provides
    fun provideResourceRepository(
        resourceDatabaseDao: ResourceDatabaseDao,
        resourceDataSource: ResourceDataSource,
    ) = ResourceRepository(
        resourceDatabaseDao,
        resourceDataSource
    ) as ResourceRepositoryInterface

    @Singleton
    @Provides
    fun provideChatRepository(
        chatDataSource: ChatDataSourceLocal
    ) = ChatRepository(
        chatDataSource
    ) as ChatRepositoryInterface

//    @Singleton
//    @Provides
//    fun provideEventAPI(): EventApi {
//        return Retrofit.Builder()
//            .baseUrl(Constants.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(EventApi::class.java)
//    }

}