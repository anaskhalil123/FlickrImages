package com.a.khalil.flickrbrowser

import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject

class GetFlickrJsonData(private val listener: OnDataAvailable) :
    AsyncTask<String, Void, ArrayList<Photo>>() {

    private val TAG = "GetFlickrJsonData"

    interface OnDataAvailable {
        fun onDataAvailable(result: ArrayList<Photo>)
        fun onError(e: JSONException)
    }

    override fun doInBackground(vararg params: String): ArrayList<Photo> {
        Log.d(TAG, "doInBackground starts")

        val photoList = ArrayList<Photo>()
        try {
            val jsonData = JSONObject(params[0])
            val itemsArray = jsonData.getJSONArray("items")

            for (i in 0 until itemsArray.length()) {
                /*
                length = number of the values in the JASONArray,
                 but until do as this -->for (i in 0..itemsArray.length()-1)
                */
                val jsonPhoto = itemsArray.getJSONObject(i)
                val title = jsonPhoto.getString("title")
                val author = jsonPhoto.getString("author")
                val authorID = jsonPhoto.getString("author_id")
                val tags = jsonPhoto.getString("tags")

                val photoUrl = jsonPhoto.getJSONObject("media").getString("m")

                val link = photoUrl
                    .replaceFirst("_m.jpg", "_b.jpg")
                /* we replace _m.jpg for _b.jpg, So we got the picture in a larger size*/
                val photoObject = Photo(title, author, authorID, link, tags, photoUrl)
                photoList.add(photoObject)
                Log.d(TAG, ".doInBackground $photoObject")
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.e(TAG, ".doInBackground: Error processing Json data . ${e.message}")
            cancel(true)
            listener.onError(e)
        }
        return photoList
    }

    override fun onPostExecute(result: ArrayList<Photo>) {
        Log.d(TAG, "onPostExecute starts")
        listener.onDataAvailable(result)
    }
}