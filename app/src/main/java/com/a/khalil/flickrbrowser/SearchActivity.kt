package com.a.khalil.flickrbrowser

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import androidx.preference.PreferenceManager

class SearchActivity : BaseActivity() {
    private val TAG = "SearchActivity"
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        activateToolbar(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        val searchableInfo = searchManager.getSearchableInfo(this.componentName)
        searchView?.setSearchableInfo(searchableInfo)

        searchView?.isIconified = false
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                Log.d(TAG, ".onQueryTextSubmit: called,with $p0")

                val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                sharedPref.edit().putString(FLICKER_QUERY, p0).apply()
                searchView?.clearFocus()
                finish()
                return true
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })

        searchView?.setOnCloseListener(object : SearchView.OnCloseListener{
            override fun onClose(): Boolean {
                finish()
                return false
            }
        })
        return true
    }
}