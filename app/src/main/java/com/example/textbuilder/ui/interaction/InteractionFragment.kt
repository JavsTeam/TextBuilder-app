package com.example.textbuilder.ui.interaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.textbuilder.R


class InteractionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_interaction, container, false)
        val spinner: Spinner = rootView.findViewById(R.id.interaction_fragment_spinner)


        val sourceList = resources.getStringArray(R.array.source_list)
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.interaction_spinner_item,
            sourceList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.adapter = adapter


        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?, // wtf its null
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }




        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}