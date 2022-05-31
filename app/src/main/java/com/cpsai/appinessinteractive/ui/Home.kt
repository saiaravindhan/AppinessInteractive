package com.cpsai.appinessinteractive.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cpsai.appinessinteractive.R
import com.cpsai.appinessinteractive.adapter.PostAdappter
import com.cpsai.appinessinteractive.databinding.ActivityHomeBinding
import com.cpsai.appinessinteractive.model.HierarchyX
import com.cpsai.appinessinteractive.model.PostsResponse
import com.cpsai.appinessinteractive.utility.Utility
import com.cpsai.appinessinteractive.viewmodel.FeedViewModel
import com.cpsai.appinessinteractive.viewmodel.FilterEvent
import com.cpsai.appinessinteractive.viewmodelfactory.FeedViewModelFactory
import com.cpsai.zohotask.network.ApiHelper
import com.cpsai.zohotask.network.RetrofitBuilder
import retrofit2.Response

class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewmodel: FeedViewModel
    private var postList: ArrayList<HierarchyX>?= ArrayList()
    private var mAdapter : PostAdappter? = null
    private val utility = Utility()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)
        viewmodel = ViewModelProvider(this, FeedViewModelFactory(ApiHelper(RetrofitBuilder.apiServie))).get(FeedViewModel::class.java)
        binding.postsRecyclerView.layoutManager = LinearLayoutManager(this)
        val itemDecorator = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        this.let { ContextCompat.getDrawable(it, R.drawable.line_divider)?.let { itemDecorator.setDrawable(it) } }
        binding.postsRecyclerView.addItemDecoration(itemDecorator)
        binding.searchBar.isIconified = false
        setObservables()
    }

    private fun setObservables(){
        viewmodel.postResponse.observe(this,{ handleResponse(it) })
    }

    private fun handleResponse(event: FilterEvent){
        when(event){
            is FilterEvent.FilterSuccessEvent -> event.response?.let { procesSuccessData(it) }
            is FilterEvent.FilterFailureEvent -> procesFailureData(event.response)
        }
    }


    private fun procesSuccessData(response: PostsResponse) {
        val data = response.dataObject
        var hierarchy: Int ?= null
        for(i in 0 until data.size){
            hierarchy = data.get(i).hierarchyList.get(i).hierarchy.size
        }
        for(j in 0 until hierarchy!!){
            Log.e("Datas",""+data.get(0).hierarchyList.get(0).hierarchy.get(j))
            postList?.add(data.get(0).hierarchyList.get(0).hierarchy.get(j))
        }
        Log.e("POST LIST",""+postList)
        mAdapter = postList?.let { PostAdappter(it, this) }
        binding.postsRecyclerView.adapter = mAdapter


    }

    private fun procesFailureData(response: Response<PostsResponse>) {
        Toast.makeText(this,"Something went wrong", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()

        if(utility.isOnline(this)){
            binding.nointernet.visibility = View.GONE
            binding.postsRecyclerView.visibility = View.VISIBLE
            viewmodel.fetchPost()
        }
        else{
            binding.nointernet.visibility = View.VISIBLE
            binding.postsRecyclerView.visibility = View.GONE
        }


        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mAdapter?.getFilter()?.filter(query)
                binding.postsRecyclerView.visibility = View.VISIBLE
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                mAdapter?.getFilter()?.filter(newText)
                binding.postsRecyclerView.visibility = View.VISIBLE
                return true
            }
        })
    }

    override fun onStop() {
        super.onStop()
        postList?.clear()
    }


}