package com.textbuilder.ui.display

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.textbuilder.R
import com.textbuilder.db.CardsDatabase
import com.textbuilder.db.FavoriteCardsDatabase
import com.textbuilder.db.providers.CardHandler
import com.textbuilder.service.Logger
import com.textbuilder.ui.display.recyclerview.Adapter
import com.textbuilder.ui.display.recyclerview.Card

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
        val handler = CardHandler(FavoriteCardsDatabase(requireContext()))
        setRecyclerViewAdapter(handler.getAllCards())
    }

    fun displayAll() {
        setRecyclerViewAdapter(getCardsData())
    }

    private fun setRecyclerViewAdapter(cardData: ArrayList<Card>) {
        recyclerView?.adapter = Adapter(cardData, requireContext())
    }

    private fun getCardsData(): ArrayList<Card> {
        val db = CardsDatabase(requireContext())
        return CardHandler(db).getAllCards()
    }
}
