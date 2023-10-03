package com.example.midtermproject.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.midtermproject.R
import com.example.midtermproject.adapter.FirstPageAdapter
import com.example.midtermproject.model.GameClass
import com.example.midtermproject.viewmodel.GameViewModel

// View Class - First Page
class FirstPageFragment : Fragment() {
    private lateinit var viewModel: GameViewModel //created view model field
    private val recyclerViewAdapter = FirstPageAdapter(arrayListOf()) //List Adapter created
    private lateinit var search: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // viewModel
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        viewModel.refreshhData() //


        val favButton = view.findViewById<LinearLayout>(R.id.favButton)

        //recyclerView
        val myRecyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        myRecyclerView.layoutManager = LinearLayoutManager(context)

        myRecyclerView.adapter = recyclerViewAdapter //recyclerView adapted bind



        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)

        // kaydırarak yenileme
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshhData()
            swipeRefreshLayout.isRefreshing = false
        }

        observeLiveData(myRecyclerView) // recyclerviewdaki  öğeler başlatılır

        search = view.findViewById<SearchView>(R.id.gameSearchBar)
        search.clearFocus() //Arama çubuğunu temizle
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false

            }

            override fun onQueryTextChange(p0: String?): Boolean {
                val gameList = viewModel.games.value
                if (p0!!.length > 3) { //Sorgu 3 karakterden uzunsa
                    filter(p0!!, gameList!!)
                } else if (p0.length!! == 0) {
                    filter(p0!!, gameList!!)
                }

                return false
            }

        })


        favButton.setOnClickListener() {
            val action =
                FirstPageFragmentDirections.actionGameFragmentToFavoritesFragment() // created an action
            Navigation.findNavController(it).navigate(action)
        }




    }

    fun ImageView.downloadImage(url: String?, placeholder: CircularProgressDrawable){

        val options = RequestOptions().placeholder(placeholder).error(R.mipmap.ic_launcher_round)

        Glide.with(context).setDefaultRequestOptions(options).load(url).into(this)

    }

    fun createPlaceHolder(context: Context) : CircularProgressDrawable {
        return CircularProgressDrawable(context).apply {
            start()
        }
    }

    private fun filter(text: String, a: List<GameClass>) {
        // filtrelemek için yeni arary oluşturulur
        val filteredlist: ArrayList<GameClass> = ArrayList()

        // tüm öğeleri tara
        for (item in a) {

            // String recyclerViewdaki herhangi bir nesneyle eşleşiyorsa
            if (item.name!!.lowercase()!!.contains(text.lowercase())) {
                // FilterList'e ekle
                filteredlist.add(item)
            }
        }
        if (!filteredlist.isEmpty()) {

            // Boşsa direkt filteredlisti döndür
            recyclerViewAdapter.filterList(filteredlist)
        }
    }


    fun observeLiveData(myRecyclerView: RecyclerView) {
        viewModel.games.observe(viewLifecycleOwner, Observer { games ->
            games?.let { // RecyclerView günellendiğinde observer çalışır
                // Observer çalıştığında, eğer öğeler boş değilse,
                // myRecyclerView'un görünürlüğü View.VISIBLE olarak ayarlanır
                myRecyclerView.visibility = View.VISIBLE
                recyclerViewAdapter.updateDataList(games)
                // ve recyclerViewAdapter sınıfına
                // güncel öğeler verilerek adaptörün öğelerini güncellemesi sağlanır.

            }
        })


    }




}