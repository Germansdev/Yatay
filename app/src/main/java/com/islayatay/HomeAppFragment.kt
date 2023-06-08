package com.islayatay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.islayatay.databinding.FragmentApphomeBinding

class HomeAppFragment : Fragment() {

    // Binding object instance corresponding to the fragment_home.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment./

    private var _binding: FragmentApphomeBinding? = null

    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment sin dataBinding true
        //return inflater.inflate(R.layout.fragment_home, container, false)

        //con dataBinding true
        _binding = FragmentApphomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view



    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding

        binding?.BtnMap?.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_apphomeFragment_to_mapFragment)
        }

        binding?.BtnCabin?.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_apphomeFragment_to_cabinsFragment)
        }

        binding.BtnResservar.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_apphomeFragment_to_authActivity)
        }
        binding.BtnLogin.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_apphomeFragment_to_authActivity)
        }



        /**binding?.BtnResservar?.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_apphomeFragment_to_reservaFragment)
        }
        */

    }



        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }




}