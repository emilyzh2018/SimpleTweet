package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose : EditText
    lateinit var btnTweet: Button

    lateinit var client: TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet= findViewById(R.id.btnTweet)

        client = TwitterApplication.getRestClient(this)
//handles users click on tweet button
        btnTweet.setOnClickListener {

            //grab content of the edit text (etCompose)
            val tweetContent = etCompose.text.toString()
            //1. make sure the tweet isnt empty
            if (tweetContent.isEmpty()){
                Toast.makeText(this,"Empty tweets not allowed!", Toast.LENGTH_SHORT).show()
                //try snackbar msg (goes away after specific # of seconds
            }else
            //2. fulfills character count
            if (tweetContent.length > 280 ) {
                Toast.makeText(this, "tweet is too long! limit is 280 char", Toast.LENGTH_SHORT)
                    .show()
            } else {

               client.publishTweet(tweetContent,object : JsonHttpResponseHandler (){
                   override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {

                       Log.i(TAG,"Successfully published tweet")
                       //send tweet back to timeline activity

                       val tweet = Tweet.fromJson(json.jsonObject)

                       val intent = Intent()
                       intent.putExtra("tweet",tweet )
                       setResult(RESULT_OK,intent)
                       finish()


                   }

                   override fun onFailure(
                       statusCode: Int,
                       headers: Headers?,
                       response: String?,
                       throwable: Throwable?
                   ) {
                       Log.e(TAG, "Failed to publish tweet", throwable)
                   }




               })
            }

            //make an api call to twitter to publish tweet

        }
    }
    companion object {
        val TAG = "ComposeActivity"
    }
}