package com.example.textbuilder.ui.readysource.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.textbuilder.R

class Adapter(private val data: List<CardData>) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    private val maxLength = 200

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            var mainTextView: TextView? = null
            var likeButton: ImageButton? = null
            var copyButton: ImageButton? = null
            var shareButton: ImageButton? = null
            var unfoldLayout: LinearLayout? = null
            var foldLayout: LinearLayout? = null

            init {
                mainTextView = itemView.findViewById(R.id.card_text_main)
                likeButton = itemView.findViewById(R.id.card_button_like)
                copyButton = itemView.findViewById(R.id.card_button_copy)
                shareButton = itemView.findViewById(R.id.card_button_share)
                unfoldLayout = itemView.findViewById(R.id.card_layout_unfold)
                foldLayout = itemView.findViewById(R.id.card_layout_fold)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_item, parent, false)
        val holder = ViewHolder(itemView)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(data[position].text.length > maxLength) { // text is too long
            holder.mainTextView?.text = data[position].text.substring(0, maxLength) + "...";
            holder.foldLayout?.visibility = View.GONE

            holder.unfoldLayout?.setOnClickListener {
                holder.mainTextView?.text = data[position].text
                holder.unfoldLayout?.visibility = View.GONE
                holder.foldLayout?.visibility = View.VISIBLE
            }
            holder.foldLayout?.setOnClickListener {
                holder.mainTextView?.text = data[position].text.substring(0, maxLength) + "...";
                holder.unfoldLayout?.visibility = View.VISIBLE
                holder.foldLayout?.visibility = View.GONE
            }

        } else { // all ok
            holder.unfoldLayout?.visibility = View.GONE
            holder.foldLayout?.visibility = View.GONE
            holder.mainTextView?.text = data[position].text
        }
    }



    override fun getItemCount(): Int {
        return data.size
    }
}
