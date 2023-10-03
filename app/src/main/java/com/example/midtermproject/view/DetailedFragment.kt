package com.example.midtermproject.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.midtermproject.viewmodel.DetailedViewModel
import com.example.midtermproject.viewmodel.FavoriteViewModel
import com.example.midtermproject.R


class DetailedFragment : Fragment() {
    private lateinit var viewModel: DetailedViewModel // viewmodel oluşturma
    private var gameId = 0 // Image Id
    private var gameIm = "" // Image Id
    private var gameFlag = false // Flag


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detailed, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //For the fragment replace button bind with view id
        val singleton = FavoriteViewModel.FavList

        arguments?.let {
            gameId = DetailedFragmentArgs.fromBundle(it).gameID
            gameIm = DetailedFragmentArgs.fromBundle(it).imageURL
            gameFlag = DetailedFragmentArgs.fromBundle(it).gameFlag
        }


        // viewModel
        viewModel = ViewModelProvider(this)[DetailedViewModel::class.java]
        viewModel.getData(gameId.toString())
        view?.findViewById<ImageView>(R.id.detailedIm)?.downloadImage(gameIm)
        val game = viewModel.detGameLiveData.value
        if (singleton.favorites.contains(game)) {
            view.findViewById<TextView>(R.id.favoriteText).setText("Favourited")
        }


        observeLiveData()

        //Favorilere ekleme ve çıkarma
        view.findViewById<LinearLayout>(R.id.addFav).setOnClickListener {
            val game = viewModel.detGameLiveData.value
            if (!singleton.favorites.contains(game)) {
                val game = viewModel.detGameLiveData.value
                singleton.favorites.add(game!!)
                view.findViewById<TextView>(R.id.favoriteText).text = "Favourited"
            } else {
                singleton.favorites.remove(game)
                view.findViewById<TextView>(R.id.favoriteText).text = "Favourite"
            }

        }

        // with navigation framework fragment replacements are done
        view.findViewById<LinearLayout>(R.id.backButton).setOnClickListener {
            if (gameFlag) {
                //GamePage sayfasındaysak game pagee dönsün
                val action =
                    DetailedFragmentDirections.actionDescFragmentToGameFragment() //action created
                Navigation.findNavController(it).navigate(action)
            } else {
                //Favori sayfasındaysak favorilere dönsün
                val action =
                    DetailedFragmentDirections.actionDescFragmentToFavoritesFragment() //action created
                Navigation.findNavController(it).navigate(action)
            }

        }

        view.findViewById<LinearLayout>(R.id.reddit).setOnClickListener{
            openBrowser()
        }
        view.findViewById<LinearLayout>(R.id.website).setOnClickListener{
            openBrowser()
        }
    }


    fun ImageView.downloadImage(url: String?){

        val options = RequestOptions().error(R.mipmap.ic_launcher_round)

        Glide.with(context).setDefaultRequestOptions(options).load(url).into(this)

    }

    fun openBrowser(){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))
        startActivity(browserIntent)
    }

    // İşlev içinde, görünüm modelinin
    // "detGameLiveData" öğesindeki veriler kullanılarak
    // birkaç metin görüntüsünün (TextView) içerikleri değiştirilir.
    // Ayrıca, verinin açıklaması içindeki HTML etiketleri çözümlenir
    // ve metin görüntüsüne atanır.
    fun observeLiveData() {

        viewModel.detGameLiveData.observe(viewLifecycleOwner, Observer { game ->
            game?.let {
                var gameName = view?.findViewById<TextView>(R.id.gameName)
                var gameDesc = view?.findViewById<TextView>(R.id.gameDesc)
                val decoded: String = Html
                    .fromHtml(it.description, Html.FROM_HTML_MODE_COMPACT)
                    .toString()
                gameName?.text = it.name
                gameDesc?.text = decoded
            }

        })
    }

}