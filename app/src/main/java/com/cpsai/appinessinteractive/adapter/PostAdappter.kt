package com.cpsai.appinessinteractive.adapter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.cpsai.appinessinteractive.R
import com.cpsai.appinessinteractive.model.HierarchyX
import kotlinx.android.synthetic.main.item_holder_post.view.*
import java.util.*
import kotlin.collections.ArrayList


class PostAdappter(var list: ArrayList<HierarchyX>, val activity: Activity): RecyclerView.Adapter<PostAdappter.MyViewHolder>() {


    val initialPostList = ArrayList<HierarchyX>().apply {
        list.let { addAll(it) }
    }

    inner class MyViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding) {
        fun bind(list: HierarchyX){
            binding.name.text = list.firstName
            binding.title.text = list.categoryName
            binding.icon_msg.setOnClickListener(View.OnClickListener {
                val smsIntent = Intent(Intent.ACTION_MAIN)
                smsIntent.addCategory(Intent.CATEGORY_APP_MESSAGING)
                smsIntent.type = "vnd.android-dir/mms-sms"
                smsIntent.putExtra("address", list.phoneNumber)
                activity.startActivity(Intent.createChooser(smsIntent,"Send SMS"))
            })


            binding.icon_phn.setOnClickListener(View.OnClickListener {
                val dialIntent = Intent(Intent.ACTION_DIAL)
                dialIntent.data = Uri.parse("tel:" + list.phoneNumber)
                activity.startActivity(dialIntent)
            })



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var root = LayoutInflater.from(parent.context).inflate(R.layout.item_holder_post, parent, false)
        return  MyViewHolder(root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list.get(position))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getFilter(): Filter {
        return PostFilter
    }

    private val PostFilter = object: Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: ArrayList<HierarchyX> = ArrayList()
            if (constraint == null || constraint.isEmpty()) {
                initialPostList.let { filteredList.addAll(it) }
            } else {
                val query = constraint.toString().trim().toLowerCase()
                initialPostList.forEach {
                    if (it.firstName.toLowerCase(Locale.ROOT).contains(query)) {
                        filteredList.add(it)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results?.values is ArrayList<*>) {
                list.clear()
                list.addAll(results.values as ArrayList<HierarchyX>)
                notifyDataSetChanged()
            }
        }
    }
}