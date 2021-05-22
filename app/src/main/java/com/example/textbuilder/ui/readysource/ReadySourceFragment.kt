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
import com.example.textbuilder.ui.readysource.recyclerview.CardData
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList


class ReadySourceFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_ready_source, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView = view.findViewById(R.id.ready_source_fragment_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = Adapter(getCardsData())

        super.onViewCreated(view, savedInstanceState)
    }

    private fun getCardsData() : ArrayList<CardData> {
        val data = ArrayList<CardData>()
        (0..15).forEach { i -> data.add(CardData(getRandomText(i))) }
        return data
    }

    private fun getRandomText(seed: Int) : String {
        val result = StringBuilder()
        repeat((0..(20 - seed)).count()) {
            (0..seed * 5).forEach { i ->
                result.append((kotlin.random.Random(i*it).nextInt() % 100 + 20).toChar()
                )
            }
        }
        return result.toString()
    }

}
