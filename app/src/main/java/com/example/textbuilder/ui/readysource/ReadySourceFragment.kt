package com.example.textbuilder.ui.readysource

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.textbuilder.R
import com.example.textbuilder.ui.readysource.recyclerview.Adapter
import com.example.textbuilder.ui.readysource.recyclerview.Card
import java.lang.StringBuilder
import kotlin.collections.ArrayList


class ReadySourceFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    var data: ArrayList<Card>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_ready_source, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.ready_source_fragment_recycler_view)
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        val localData = getCardsData()
        data = localData
        setRecyclerViewAdapter(localData)

        super.onViewCreated(view, savedInstanceState)
    }

    fun displayFavorite() {
        setRecyclerViewAdapter(getFavoriteCards(getCardsData()))
    }

    fun displayAll() {
        setRecyclerViewAdapter(getCardsData())
    }

    private fun setRecyclerViewAdapter(cardData: ArrayList<Card>) {
        recyclerView?.adapter = Adapter(cardData)
    }

    private fun getFavoriteCards(cards: ArrayList<Card>): ArrayList<Card> {
        val result: ArrayList<Card> = ArrayList()
        for(card: Card in cards) {
            if(card.isFavorite) {
                result.add(card)
            }
        }
        return result
    }

    private fun getCardsData(): ArrayList<Card> {
        val data = ArrayList<Card>()
        (0..15).forEach { i -> data.add(Card(getRandomText(i))) }
        return data
    }

    private fun getRandomText(seed: Int): String {
        val result = StringBuilder()
        repeat((0..(20 - seed)).count()) {
            (0..seed * 5).forEach { i ->
                result.append(
                    (kotlin.random.Random(i * it).nextInt() % 100 + 20).toChar()
                )
            }
        }
        return result.toString()
    }
}
