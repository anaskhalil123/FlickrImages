package com.a.khalil.flickrbrowser

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FlickerImageViewHolder(private var view: View) : RecyclerView.ViewHolder(view) {
    var thumbnail: ImageView = view.findViewById(R.id.thumbnail)
    var title: TextView = view.findViewById(R.id.title)
}


class FlickerRecyclerViewAdapter(private var photoList: List<Photo>) :
    RecyclerView.Adapter<FlickerImageViewHolder>() {

    private val TAG =
        "FlickerRecyclerViewAd"// if Tag characters more than 23 the Log will appear an error

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlickerImageViewHolder {
        // Called by the layout manager when it needs a new view
//        Log.d(TAG, ".onCreateViewHolder new view requested")
        val root = LayoutInflater.from(parent.context).inflate(R.layout.browse, parent, false)
        return FlickerImageViewHolder(root)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadNewData(newPhotos: List<Photo>) {
        photoList = newPhotos
        notifyDataSetChanged()
    }

    fun getPhoto(position: Int): Photo? {
        return if (photoList.isNotEmpty()) photoList[position] else null
    }

    override fun onBindViewHolder(holder: FlickerImageViewHolder, position: Int) {

        if (photoList.isEmpty()) {
            holder.thumbnail.setImageResource(R.drawable.placeholder)
            holder.title.setText(R.string.empty_photo)
        } else {

            val photoItem = photoList[position]

            Log.d("Anas", photoItem.title)
            Log.d("Anas", photoItem.image)

            Picasso.get().load(photoItem.image)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbnail)

            holder.title.text = photoList[position].title
        }
    }

    override fun getItemCount(): Int {
        return if (photoList.isNotEmpty()) photoList.size else 1
    }


}