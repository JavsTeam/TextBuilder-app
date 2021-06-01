package com.textbuilder.ui.display

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.textbuilder.R
import com.textbuilder.db.CardsDatabase
import com.textbuilder.db.FavoriteCardsDatabase
import com.textbuilder.db.providers.CardHandler
import com.textbuilder.ui.display.recyclerview.Adapter
import com.textbuilder.ui.display.recyclerview.Card
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DisplayFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    var data: ArrayList<Card>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_display, container, false)
        data = getCardsData()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.display_fragment_recycler_view)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        super.onViewCreated(view, savedInstanceState)
    }

    fun displayFavorite() {
        setRecyclerViewAdapter(getFavoriteCards(getCardsData()))

        val cardHandler = CardHandler(FavoriteCardsDatabase(requireContext()))
        setRecyclerViewAdapter(cardHandler.getAllCards())
    }

    fun displayAll() {
        setRecyclerViewAdapter(getCardsData())
    }

    private fun setRecyclerViewAdapter(cardData: ArrayList<Card>) {
        Thread.sleep(20) // crutch to wait for DB access
        recyclerView?.adapter = Adapter(cardData, requireContext())
    }

    private fun getFavoriteCards(cards: ArrayList<Card>): ArrayList<Card> {
        Thread.sleep(20) // Waiting for db
        val result: ArrayList<Card> = ArrayList()
        for (card: Card in cards) {
            if (card.isFavorite) {
                result.add(card)
            }
        }
        return result
    }

    private fun getCardsData(): ArrayList<Card> {
        val data = ArrayList<Card>()
        val db = CardsDatabase(requireContext())
        GlobalScope.launch {
            val dataFromDB = db.cardsDao().getAll()
            dataFromDB.forEach {
                data.add(
                    Card(
                        it.id,
                        it.title,
                        it.content,
                        it.isFavorite
                    )
                )
            }
        }
        Thread.sleep(50)
        return data
    }
}
