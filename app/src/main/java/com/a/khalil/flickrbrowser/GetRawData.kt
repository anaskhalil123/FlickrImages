package com.a.khalil.flickrbrowser

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

enum class DownloadStatus {
    OK,
    IDLE/*means not processing any thing */,
    NOT_INITIALISED/*the URL is invalid*/,
    FAILED_OR_EMPTY/*Either we failed to download a thing, or the data come back empty*/,
    PERMISSIONS_ERROR/**/,
    ERROR
}

class GetRawData(private val listener: OnDownloadComplete) : AsyncTask<String, Void, String>() {
    private val TAG = "GetRawData"
    private var downloadStatus = DownloadStatus.IDLE

    interface OnDownloadComplete {
        fun onDownloadComplete(result: String, downloadStatus: DownloadStatus)
    }

    override fun doInBackground(vararg params: String?): String {
        /*params==> can not be null, but params elements can be null*/
        if (params[0] == null) {
            downloadStatus = DownloadStatus.NOT_INITIALISED
            return "No URL specified"
        }

        try {
            Log.d(TAG, URL(params[0]).toString())
            downloadStatus = DownloadStatus.OK
            return URL(params[0]).readText()
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is MalformedURLException -> {
                    downloadStatus = DownloadStatus.NOT_INITIALISED
                    "MalformedURLException: ${e.message}"
                }
                is IOException -> {
                    downloadStatus = DownloadStatus.FAILED_OR_EMPTY
                    "IOException: ${e.message}"
                }
                is SecurityException -> {
                    downloadStatus = DownloadStatus.PERMISSIONS_ERROR
                    "SecurityException: ${e.message}"
                }
                else -> {
                    downloadStatus = DownloadStatus.ERROR
                    "Exception: ${e.message}"
                }
            }
            return errorMessage
        }
    }

    override fun onPostExecute(result: String) {
        Log.d(TAG, "onPostExecute")
        listener.onDownloadComplete(result, downloadStatus)
    }

}