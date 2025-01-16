package ro.pub.cs.systems.eim.practicaltest02v9

import retrofit2.http.GET
import retrofit2.http.Path

interface AnagramService {
    @GET("all/:{word}")
    suspend fun getAnagrams(@Path("word") word: String): retrofit2.Response<AnagramResponse>
}

data class AnagramResponse(
    val all: List<String>
)
