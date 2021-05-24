package com.example.textbuilder.ui.readysource.recyclerview

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.textbuilder.R


class Adapter(private val data: List<Card>, private val context: Context) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {
    private val maxLength = 200

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var isFavorite: Boolean = false
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
        val currentCard = data[data.size - position - 1]

        initLikeButton(holder, currentCard)
        initCopyButton(holder, currentCard)
        initFolding(holder, currentCard)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun initCopyButton(holder: ViewHolder, currentCard: Card) {
        holder.copyButton?.setOnClickListener {
            makeToast("Скопировано!")
            context.copyToClipboard(currentCard.text)
        }
    }

    private fun Context.copyToClipboard(text: CharSequence) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", text)
        clipboard.setPrimaryClip(clip)
    }

    private fun initLikeButton(holder: ViewHolder, currentCard: Card) {
        holder.isFavorite = currentCard.isFavorite

        if (holder.isFavorite) {
            holder.likeButton?.setImageResource(R.drawable.ic_favorite_filled)
        } else {
            holder.likeButton?.setImageResource(R.drawable.ic_favorite)
        }

        holder.likeButton?.setOnClickListener {
            if (holder.isFavorite) {
                holder.likeButton?.setImageResource(R.drawable.ic_favorite)
            } else {
                holder.likeButton?.setImageResource(R.drawable.ic_favorite_filled)
            }
            holder.isFavorite = !holder.isFavorite
        }
    }

    private fun initFolding(holder: ViewHolder, currentCard: Card) {
        if (currentCard.text.length > maxLength) { // text is too long
            holder.mainTextView?.text = currentCard.text.substring(0, maxLength) + " ...";
            holder.foldLayout?.visibility = View.GONE
            holder.unfoldLayout?.setOnClickListener {
                holder.mainTextView?.text = currentCard.text
                holder.unfoldLayout?.visibility = View.GONE
                holder.foldLayout?.visibility = View.VISIBLE
            }
            holder.foldLayout?.setOnClickListener {
                holder.mainTextView?.text = currentCard.text.substring(0, maxLength) + "...";
                holder.unfoldLayout?.visibility = View.VISIBLE
                holder.foldLayout?.visibility = View.GONE
            }
        } else { // all ok
            holder.unfoldLayout?.visibility = View.GONE
            holder.foldLayout?.visibility = View.GONE
            holder.mainTextView?.text = currentCard.text
        }
    }

    private fun makeToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}
