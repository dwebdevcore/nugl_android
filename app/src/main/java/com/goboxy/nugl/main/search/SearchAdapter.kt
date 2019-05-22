package com.goboxy.nugl.main.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goboxy.nugl.R
import com.goboxy.nugl.listingdetail.ListingDetailActivity

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_listing_view, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
    }

    class SearchViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView?.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            v?.context?.let { ListingDetailActivity.start(it) }
        }
    }

}