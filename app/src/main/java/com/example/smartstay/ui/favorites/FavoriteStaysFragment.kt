package com.example.smartstay.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.smartstay.databinding.FragmentFavoritesBinding

class FavoriteStaysFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val favoriteStaysViewModel = ViewModelProvider(this).get(FavoriteStaysViewModel::class.java)

        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        favoriteStaysViewModel.text.observe(viewLifecycleOwner) {
            binding.textFavoriteStays.text = it
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}