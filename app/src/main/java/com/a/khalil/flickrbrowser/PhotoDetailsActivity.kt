package com.a.khalil.flickrbrowser

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class PhotoDetailsActivity : BaseActivity() {
    private val TAG = "PhotoDetailsActivity"
    private val tv_photoAuthor: TextView by lazy { findViewById(R.id.tv_photoAuthor) }
    private val tv_photoTitle: TextView by lazy { findViewById(R.id.tv_photoTitle) }
    private val tv_photoTags: TextView by lazy { findViewById(R.id.tv_photoTags) }
    private val img_photoImage: ImageView by lazy { findViewById(R.id.img_photoImage) }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_details)
        activateToolbar(true)

        val photo = intent.extras!!.getParcelable<Photo>(PHOTO_TRANSFER) as Photo

        if (!photo.equals(null)) {
            tv_photoTitle.text = "Title : ${photo.title}"
            tv_photoAuthor.text = "Author : ${photo.author}"
            tv_photoTags.text = "Tag : ${photo.tags}"

            Picasso.get().load(photo.link)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(img_photoImage)

        }

    }
}