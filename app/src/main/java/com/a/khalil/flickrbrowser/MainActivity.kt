package com.a.khalil.flickrbrowser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONException

class MainActivity : BaseActivity(), GetRawData.OnDownloadComplete,
    GetFlickrJsonData.OnDataAvailable, RecyclerItemClickListener.OnRecyclerClickListener {

    private val TAG = "MainActivity"
    private val flickerRecyclerViewAdapter = FlickerRecyclerViewAdapter(ArrayList())
    private val recycler_view: RecyclerView by lazy { findViewById<RecyclerView>(R.id.recycler_view) }
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activateToolbar(false)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addOnItemTouchListener(
            RecyclerItemClickListener(
                this,
                recycler_view,
                this
            )
        )
        recycler_view.adapter = flickerRecyclerViewAdapter


    }

    private fun createUri(
        baseURL: String,
        searchCriteria: String,
        lang: String,
        matchAll: Boolean
    ): String {

        return Uri.parse(baseURL).buildUpon()
            .appendQueryParameter("tags", searchCriteria)
            .appendQueryParameter("tagmode", if (matchAll) "ALL" else "ANY")
            .appendQueryParameter("lang", lang)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1").build().toString()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d(TAG, "onCreateOptionsMenu called")
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected called")
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDownloadComplete(result: String, downloadStatus: DownloadStatus) {
        if (downloadStatus == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete called")
            val getFlickrJsonData = GetFlickrJsonData(this)
            getFlickrJsonData.execute(result)
        } else {
            Log.d(
                TAG,
                "onDownloadComplete filed, with status $downloadStatus, Error message is: $result"
            )
        }
    }

    override fun onDataAvailable(result: ArrayList<Photo>) {
        Log.d(TAG, ".onDataAvailable")
        flickerRecyclerViewAdapter.loadNewData(result)
    }

    override fun onError(e: JSONException) {
        Log.e(TAG, "onError called with Exception ${e.message}")
    }

    override fun onItemClick(view: View, position: Int) {
        Log.d(TAG, "onItemClick starts")
        Toast.makeText(this, "Normal tap at $position", Toast.LENGTH_SHORT).show()
    }

    override fun onItemLongClick(view: View, position: Int) {
        Log.d(TAG, "onItemLongClick starts")
        val photo = flickerRecyclerViewAdapter.getPhoto(position)
        if (photo != null) {
            val intent = Intent(this, PhotoDetailsActivity::class.java)
            intent.putExtra(PHOTO_TRANSFER, photo)
            startActivity(intent)
        }
    }

    override fun onResume() {
        Log.d(TAG, ".onResume: called")
        super.onResume()

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val query = sharedPref.getString(FLICKER_QUERY, "")

        if (query!!.isNotEmpty()) {
            val url = createUri(
                "https://www.flickr.com/services/feeds/photos_public.gne",
                query,
                "en-us",
                true
            )
            val getRawData = GetRawData(this)
            getRawData.execute(url)
        }
    }

}