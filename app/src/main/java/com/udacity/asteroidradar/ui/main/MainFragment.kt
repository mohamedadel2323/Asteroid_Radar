package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.adapters.AstroidsAdapter
import com.udacity.asteroidradar.api.AsteroidApiFilter
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelFactory: MainViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)

        val application = requireNotNull(this.activity).application
        val database = AsteroidDatabase.getInstance(application)

        viewModelFactory = MainViewModelFactory(database, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        val adapter = AstroidsAdapter()

        binding.asteroidRecycler.adapter = adapter

        adapter.setOnItemClickListener {
            viewModel.navigateToDetails(it)
        }

        viewModel.asteroidList.observe(viewLifecycleOwner, Observer {
            Log.e("today" , it.toString())
            it?.let {
                adapter.submitList(it)
                viewModel.doneShowingProgressBar()
            }
        })
        viewModel.navigateToDetails.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.doneNavigateToDetails()
            }
        })
        viewModel.progressBar.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.statusLoadingWheel.visibility = View.VISIBLE
            } else {
                binding.statusLoadingWheel.visibility = View.GONE
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_all_menu -> viewModel.getAllAsteroids()
            R.id.show_week_menu -> viewModel.getAsteroidsByFilter(AsteroidApiFilter.SHOW_WEEK)
            R.id.show_today_menu -> viewModel.getAsteroidsByFilter(AsteroidApiFilter.SHOW_TODAY)
        }
        return true
    }
}
