package ngodangtan.com.newsappandroidretrofit2

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ngodangtan.com.newsappandroidretrofit2.adapter.RecyclerAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.currentsapi.services"
class MainActivity : AppCompatActivity() {

    lateinit var countDownTimer: CountDownTimer

    private var titlesList = mutableListOf<String>()
    private var descList = mutableListOf<String>()
    private var imagesList = mutableListOf<String>()
    private var linksList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        makeAPIRequest()
    }

    private fun fadeInFromBlack(){
        v_blackScreen.animate().apply {
            alpha(0f)
            duration = 3000

        }.start()
    }

    private fun setUpRecyclerView(){
        rv_recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        rv_recyclerView.adapter = RecyclerAdapter(titlesList,descList,imagesList,linksList)
    }
    private fun addToList(title:String,descrition:String,image:String,link:String){
        titlesList.add(title)
        descList.add(descrition)
        imagesList.add(image)
        linksList.add(link)
    }

    private fun makeAPIRequest(){
        progressBar.visibility = View.VISIBLE
        val api = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIRequest::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getNews()
                for(article in response.news){
                    Log.i("MainActivity","Result = $article")
                    addToList(article.title,article.description,article.image,article.url)
                }
                withContext(Dispatchers.Main){
                    setUpRecyclerView()
                    fadeInFromBlack()
                    progressBar.visibility = View.GONE
                }
            }catch (e: Exception){
                Log.e("MainActivity",e.toString())
                withContext(Dispatchers.Main){
                    attemptRequestAgain()
                }
            }
        }

    }
    private fun attemptRequestAgain(){
        countDownTimer = object : CountDownTimer(5*1000,1000){
            override fun onTick(p0: Long) {
                Log.i("MainActivity","Could not retrieve data..Try agin in ${p0/1000} second")
            }

            override fun onFinish() {
                makeAPIRequest()
                countDownTimer.cancel()
            }

        }
        countDownTimer.start()
    }
}