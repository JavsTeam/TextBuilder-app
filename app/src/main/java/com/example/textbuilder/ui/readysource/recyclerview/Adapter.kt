package com.example.textbuilder.ui.readysource.recyclerview

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.textbuilder.R
import com.example.textbuilder.db.CardsDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


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

        initLikeButton(holder, currentCard, context)
        initCopyButton(holder, currentCard)
        initShareButton(holder, currentCard)
        initFolding(holder, currentCard)
    }

    private fun initShareButton(holder: ViewHolder, currentCard: Card) {
        holder.shareButton?.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val shareBody = currentCard.text
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            context.startActivity(Intent.createChooser(sharingIntent, "Share using"))
        }
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

    private fun initLikeButton(holder: ViewHolder, currentCard: Card, context: Context) {
        holder.isFavorite = currentCard.isFavorite

        if (holder.isFavorite) {
            holder.likeButton?.setImageResource(R.drawable.ic_favorite_filled)
        } else {
            holder.likeButton?.setImageResource(R.drawable.ic_favorite)
        }

        holder.likeButton?.setOnClickListener {
            if (holder.isFavorite) {
                Log.d("Debug", "Deleting from favorites")
                holder.likeButton?.setImageResource(R.drawable.ic_favorite)
                GlobalScope.launch {
                    val db = CardsDatabase(context)
                    val cardEntity = db.cardsDao().findById(currentCard.id)
                    cardEntity.isFavorite = false
                    db.cardsDao().updateCards(cardEntity)
                }
            } else {
                Log.d("Debug", "Adding to favorites")
                holder.likeButton?.setImageResource(R.drawable.ic_favorite_filled)
                GlobalScope.launch {
                    val db = CardsDatabase(context)
                    val cardEntity = db.cardsDao().findById(currentCard.id)
                    cardEntity.isFavorite = true
                    db.cardsDao().updateCards(cardEntity)
                    Log.d("Debug", "Cards updated")
                }
            }
            Log.d("Debug", "Inverting holder value")
            holder.isFavorite = !holder.isFavorite
            currentCard.isFavorite = !currentCard.isFavorite
        }
    }

    private fun initFolding(holder: ViewHolder, currentCard: Card) {
        if (currentCard.text.length > maxLength) { // text is too long
            holder.mainTextView?.text = currentCard.text.substring(0, maxLength) + " ...";
            holder.foldLayout?.visibility = View.GONE
            holder.unfoldLayout?.visibility = View.VISIBLE
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
