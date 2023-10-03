package com.example.midtermproject.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.midtermproject.adapter.FavoritesAdapter
import com.example.midtermproject.model.GameClass
import com.example.midtermproject.viewmodel.FavoriteViewModel
import com.example.midtermproject.R


// View Class Favorites Page
class FavPageFragment : Fragment() {
    var game: GameClass? = null


    private val recyclerViewAdapter = FavoritesAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val singleton = FavoriteViewModel.FavList

        //recyclerView
        val myRecyclerView = view.findViewById<RecyclerView>(R.id.gamesListFav)
        myRecyclerView.layoutManager = LinearLayoutManager(context)//recyclerView adapted bind
        myRecyclerView.adapter = recyclerViewAdapter // list elements initialized
        recyclerViewAdapter.updateDataList(singleton.favorites)
        myRecyclerView.visibility = View.VISIBLE



        val swipeToDeleteCallBack = object : SwipeToDelete() {

            //silmek istediğinden emin misin???
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val builder = context?.let { AlertDialog.Builder(it) }
                builder!!.setMessage("Are you sure you want to Delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        // Delete selected note from database
                        val position = viewHolder.adapterPosition
                        singleton.favorites.removeAt(position)
                        recyclerViewAdapter.notifyItemRemoved(position)
                        recyclerViewAdapter.updateDataList(singleton.favorites)
                        setTitle()
                    }

                    .setNegativeButton("No") { dialog, id ->
                        // Dismiss the dialog
                        dialog.dismiss()
                        recyclerViewAdapter.updateDataList(singleton.favorites)
                    }
                val alert = builder.create()
                alert.show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(myRecyclerView)

        setTitle()


        // with navigation framework fragment replacements are done
        view.findViewById<LinearLayout>(R.id.gameButtonFav).setOnClickListener {
            val action =
                FavPageFragmentDirections.actionFavoritesFragmentToGameFragment()// created an action
            Navigation.findNavController(it).navigate(action)
        }
    }

    fun setTitle() { //Eğer RecyclerViewdaeleman yoksa paranteze yazma
                    // ,varsa eleman sayısını yaz
        val count = recyclerViewAdapter.itemCount
        if (count != 0) {
            view?.findViewById<TextView>(R.id.gameTabFav)?.setText("Favourites ($count)")
        } else if (count == 0) {
            view?.findViewById<TextView>(R.id.gameTabFav)?.setText("Favourites")
        }
    }



}