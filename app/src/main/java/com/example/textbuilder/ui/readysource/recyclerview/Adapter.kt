package com.example.textbuilder.ui.readysource.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.textbuilder.R

class Adapter(private val data: List<CardData>) : RecyclerView.Adapter<Adapter.ViewHolder>() {
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            var mainTextView: TextView? = null
            var likeButton: ImageButton? = null
            var copyButton: ImageButton? = null
            var shareButton: ImageButton? = null
            var unfoldLayout: LinearLayout? = null

            init {
                mainTextView = itemView.findViewById(R.id.card_text_main)
                likeButton = itemView.findViewById(R.id.card_button_like)
                copyButton = itemView.findViewById(R.id.card_button_copy)
                shareButton = itemView.findViewById(R.id.card_button_share)
                unfoldLayout = itemView.findViewById(R.id.card_layout_unfold)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mainTextView?.text = data[position].text
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
