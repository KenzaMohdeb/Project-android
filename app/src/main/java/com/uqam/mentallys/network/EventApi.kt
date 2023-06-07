package com.uqam.mentallys.network
//package com.uqam.mentallys.network
//
//import com.uqam.mentallys.model.Event
//import com.uqam.mentallys.utils.Constants
//import retrofit2.http.GET
//import retrofit2.http.Query
//import javax.inject.Singleton
//
//@Singleton
//interface EventApi {
//    @GET(value = "data/2.5/forecast/daily")
//    suspend fun getEvents(
//        @Query("q") searchQuery: String = "",
//        @Query("apikey") apiKey: String = Constants.API_KEY
//    ): List<Event>
//}