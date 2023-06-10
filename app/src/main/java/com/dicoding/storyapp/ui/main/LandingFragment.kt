package com.dicoding.storyapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.remote.response.ListStoryItem
import com.dicoding.storyapp.databinding.FragmentLandingBinding
import com.dicoding.storyapp.helper.LoginPreference
import com.dicoding.storyapp.ui.camera.CameraArgs
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LandingFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLandingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    @Inject
    lateinit var loginPref: LoginPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!loginPref.isLogin())
            findNavController().navigate(R.id.action_landingFragment_to_loginFragment)

        _binding = FragmentLandingBinding.inflate(layoutInflater, container, false)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.m_settings -> {
                        findNavController().navigate(R.id.action_landingFragment_to_settingsFragment)
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.STARTED)

        binding.fabAddStory.setOnClickListener(this)

        showLoading()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        setupRecyclerView()
        setupViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setToolbar() {
        val toolbar = (activity as AppCompatActivity?)!!.supportActionBar!!
        toolbar.show()
        toolbar.setTitle(getString(R.string.toolbar_title))
    }

    private fun setupViewModel() {
        viewModel.getAllStory()

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.listStory.observe(viewLifecycleOwner){
            setStory(it)
        }
    }

    private fun setStory(listStory: List<ListStoryItem>) {
        if(!listStory.isEmpty()){
            val adapter = MainAdapter(listStory)
            binding.rvStory.adapter = adapter
            adapter.setOnClickCallback(object : MainAdapter.OnItemClickCallback{
                override fun onItemClicked(data: ListStoryItem, holder: MainAdapter.ViewHolder) {
                    val extra = FragmentNavigatorExtras(holder.binding.img to "img_story_big")
                    val direction = LandingFragmentDirections.actionLandingFragmentToStoryDetailFragment(
                        data.photoUrl,data.name,data.createdAt,data.description
                    )
                    findNavController().navigate(direction,extra)
                }
            })
        } else
            toastMaker(getString(R.string.belum_ada_story))
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        binding.rvStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context,layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)
    }

    private fun toastMaker(msg: String){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean = false) {
        binding.pbMain.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val TAG = "Landing fragment"
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.fab_addStory -> {
                findNavController().navigate(LandingFragmentDirections.actionLandingFragmentToAddStoryFragment(null))
            }
        }
    }
}