package com.rootdown.dev.paging_v3_1.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import androidx.databinding.BindingAdapter

/**
 * Binding adapter used to display images from URL using Glide
 */
@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String) {
    Glide.with(imageView.context).load("https://cdn.karmanomic.com/$url").into(imageView)
}