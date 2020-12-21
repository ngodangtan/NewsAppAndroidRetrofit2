package ngodangtan.com.newsappandroidretrofit2

import ngodangtan.com.newsappandroidretrofit2.api.NewsApiJSON
import retrofit2.http.GET

interface APIRequest {
    @GET("/v1/latest-news?language=it&apiKey=wNfGwaV48Nld9Pj52HgpHRC7OuwqdCP03ACd8icZNomWBqwb")
    suspend fun getNews(): NewsApiJSON
}