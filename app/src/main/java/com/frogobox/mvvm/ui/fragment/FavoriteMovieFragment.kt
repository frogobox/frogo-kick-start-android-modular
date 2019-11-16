package com.frogobox.mvvm.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.frogobox.base.modular.model.FavoriteMovie
import com.frogobox.base.modular.rvadapter.FavoriteMovieAdapter
import com.frogobox.base.ui.BaseFragment
import com.frogobox.base.ui.adapter.BaseListener
import com.frogobox.mvvm.R
import com.frogobox.mvvm.ui.activity.DetailMovieActivity
import com.frogobox.mvvm.ui.activity.MainActivity
import com.frogobox.mvvm.viewmodel.FavoriteMovieViewModel
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.fragment_tv_movie_list.*

/**
 * A simple [Fragment] subclass.
 */
class FavoriteMovieFragment : BaseFragment(),
    BaseListener<FavoriteMovie> {

    private lateinit var mViewModel: FavoriteMovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setupViewModel()
        return inflater.inflate(R.layout.fragment_tv_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMovie()
    }

    override fun onResume() {
        super.onResume()
        getMovie()
    }

    private fun getMovie() {
        mViewModel.getFavoriteMovie()
    }

    private fun setupViewModel() {
        mViewModel = (activity as MainActivity).obtainFavoriteMovieViewModel().apply {

            favMovieListLive.observe(viewLifecycleOwner, Observer {
                setupRecyclerView(it)
            })

            eventShowProgress.observe(viewLifecycleOwner, Observer {
                setupEventProgressView(progressBar, it)
            })

            eventIsEmpty.observe(viewLifecycleOwner, Observer {
                setupEventEmptyView(empty_view, it)
            })

        }
    }

    private fun setupRecyclerView(data: List<FavoriteMovie>) {
        val adapter = FavoriteMovieAdapter()
        context?.let { adapter.setRecyclerViewLayout(it, R.layout.item_list_tv_movie) }
        adapter.setRecyclerViewListener(this)
        adapter.setRecyclerViewData(data)
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }


    override fun onItemClicked(data: FavoriteMovie) {
        context?.let {
            baseStartActivity<DetailMovieActivity, FavoriteMovie>(
                it,
                DetailMovieActivity.EXTRA_FAV_MOVIE,
                data
            )
        }
    }

    override fun onItemLongClicked(data: FavoriteMovie) {

    }

}
