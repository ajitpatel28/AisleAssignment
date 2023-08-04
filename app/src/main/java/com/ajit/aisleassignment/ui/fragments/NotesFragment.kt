package com.ajit.aisleassignment.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajit.aisleassignment.R
import com.ajit.aisleassignment.databinding.FragmentNotesBinding
import com.ajit.aisleassignment.ui.adapters.InvitesAdapter
import com.ajit.aisleassignment.ui.adapters.LikesAdapter
import com.ajit.aisleassignment.ui.viewmodels.NotesViewModel
import com.ajit.aisleassignment.utils.UiState
import com.ajit.aisleassignment.utils.hide
import com.ajit.aisleassignment.utils.show
import com.ajit.aisleassignment.utils.toast
import org.koin.androidx.viewmodel.ext.android.viewModel


class NotesFragment : Fragment() {

    private lateinit var binding: FragmentNotesBinding
    private val notesViewModel: NotesViewModel by viewModel()
    private val invitesAdapter = InvitesAdapter()
    private val likesAdapter = LikesAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = NotesFragmentArgs.fromBundle(requireArguments())

        val authToken = args.token

        binding.recyclerInvites.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = invitesAdapter
        }

        binding.recyclerLikes.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = likesAdapter
        }

        notesViewModel.notesResponse.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    binding.invitesProgressBar.show()
                    binding.likesProgressBar.show()
                }
                is UiState.Success -> {
                    binding.invitesProgressBar.hide()
                    binding.likesProgressBar.hide()
                    val notesResponse = uiState.data
                    val invites = notesResponse?.invites
                    val likes = notesResponse?.likes
//                    Log.e("notes","invites $invites, likes ---$likes")
                    invites?.let {
                        val profiles = it["profiles"] as? List<Map<String, Any>>
                        profiles?.let { profilesList ->
                            invitesAdapter.submitList(profilesList)
                        }
                    }
                    likes?.let {
                        val profiles = it["profiles"] as? List<Map<String, Any>>
                        profiles?.let { profilesList ->
                            likesAdapter.submitList(profilesList)
                        }
                    }
                }
                is UiState.Failure -> {
                    toast(uiState.error)
                    binding.invitesProgressBar.hide()
                    binding.likesProgressBar.hide()
                }
            }
        }

        notesViewModel.getNotes(authToken)
    }

}
