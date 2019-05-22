package com.goboxy.nugl.main.search

import android.animation.LayoutTransition
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.*
import com.goboxy.nugl.R
import com.goboxy.nugl.main.MainBottomFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.fragment_search.*

private const val ANIMATION_DURATION = 300L

class SearchFragment : MainBottomFragment(), OnMapReadyCallback {

    enum class ActiveListView {
        LIST, MAP
    }

    companion object {

        fun newInstance(): SearchFragment {
            return SearchFragment()
        }

    }

    private var activeListView = ActiveListView.MAP
    private val pagerSnapHelper = PagerSnapHelper()
    private lateinit var adapter: SearchAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var spacesItemDecoration: SpacesItemDecoration

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchContentView.layoutTransition.setDuration(ANIMATION_DURATION)
        searchContentView.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        adapter = SearchAdapter()
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        spacesItemDecoration = SpacesItemDecoration(RecyclerView.HORIZONTAL, resources.getDimensionPixelSize(R.dimen.search_item_card_space))
        searchRecyclerView.layoutManager = layoutManager
        searchRecyclerView.adapter = adapter

        searchRecyclerView.addItemDecoration(spacesItemDecoration)
        pagerSnapHelper.attachToRecyclerView(searchRecyclerView)

        val mapFragment = SupportMapFragment.newInstance()
        childFragmentManager.beginTransaction().add(R.id.searchContainerMapView, mapFragment).commit()
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap == null) {
            return
        }

        val height = resources.getDimensionPixelSize(R.dimen.item_listing_height)
        val padding = resources.getDimensionPixelSize(R.dimen.search_item_card_padding)
        googleMap.setPadding(padding / 2, 0, 0, height + padding)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_toolbar_search, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        val menuList = menu?.findItem(R.id.menu_search_list_view)
        val menuMap = menu?.findItem(R.id.menu_search_map_view)

        menuList?.isVisible = activeListView == ActiveListView.MAP
        menuMap?.isVisible = activeListView == ActiveListView.LIST
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_search_list_view -> showList()
            R.id.menu_search_map_view -> showMap()
        }
        activity?.invalidateOptionsMenu()
        return super.onOptionsItemSelected(item)
    }

    private fun showMap() {
        activeListView = ActiveListView.MAP
        val padding = resources.getDimensionPixelSize(R.dimen.search_item_card_padding)
        searchRecyclerView.setPadding(padding, padding, padding, padding)
        pagerSnapHelper.attachToRecyclerView(searchRecyclerView)
        searchRecyclerView.addItemDecoration(spacesItemDecoration)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
    }

    private fun showList() {
        activeListView = ActiveListView.LIST
        searchRecyclerView.setPadding(0, 0, 0, 0)
        pagerSnapHelper.attachToRecyclerView(null)
        searchRecyclerView.removeItemDecoration(spacesItemDecoration)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
    }

}