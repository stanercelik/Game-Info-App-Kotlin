package com.example.midtermproject.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.midtermproject.model.GameClass

import com.example.midtermproject.view.FirstPageFragmentDirections
import com.example.midtermproject.R


class FirstPageAdapter(var dataSet: ArrayList<GameClass>) :
    RecyclerView.Adapter<FirstPageAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        val score: TextView
        val genre: TextView
        val gameImage: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            name = view.findViewById(R.id.title)
            score = view.findViewById(R.id.metacriticPoint)
            genre = view.findViewById(R.id.Genre)
            gameImage = view.findViewById(R.id.gameImage)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.games_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.name.text = dataSet.get(position).name
        viewHolder.score.text = dataSet.get(position).metacritic.toString()
        val l1 = arrayListOf<String>()
        for (item in dataSet.get(position).genres) {
            l1.add(item.name!!)

        }
        viewHolder.genre.text = l1.joinToString(separator = ", ")
        //viewHolder.desc.text = dataSet[position].description
        viewHolder.gameImage.downloadImage(
            dataSet.get(position).background_image
        )
        //viewHolder.gameImage.setImageDrawable(viewHolder.itemView.context.getDrawable(dataSet[position].gameImage))


        /* with navigation framework fragment replacements are done
        * For the clicking on the elements in the list this process done in adapter class
        */
        viewHolder.itemView.setOnClickListener {
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#E0E0E0"))
            val action = FirstPageFragmentDirections.actionGameFragmentToDescFragment(
                dataSet.get(position).id,
                dataSet.get(position).background_image,
                true
            )
            Navigation.findNavController(it).navigate(action)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    // Return the new dataset
    fun updateDataList(newList: List<GameClass>) {
        dataSet.clear()
        dataSet.addAll(newList)
        notifyDataSetChanged()
    }

    fun filterList(filterlist: ArrayList<GameClass>) {
        // below line is to add our filtered
        // list in our course array list.
        dataSet = filterlist
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }

    fun ImageView.downloadImage(url: String?){

        val options = RequestOptions().error(R.mipmap.ic_launcher_round)

        Glide.with(context).setDefaultRequestOptions(options).load(url).into(this)

    }

}