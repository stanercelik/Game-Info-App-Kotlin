package com.example.midtermproject.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.midtermproject.model.GameClass
import com.example.midtermproject.service.GamesAPIService
import com.example.midtermproject.service.Responses
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver


/* ViewModel - FirstPage Class */
class GameViewModel: ViewModel() {


    val games = MutableLiveData<List<GameClass>?>()

    val gameErrMsg = MutableLiveData<Boolean>()
    val gameDownloading = MutableLiveData<Boolean>()

    private val gameApiService = GamesAPIService()
    private val disposable = CompositeDisposable()



    fun refreshhData(){
        getDataFromInternet()

    }

    fun getDataFromInternet(){
        gameDownloading.value = true
        disposable.add(
            gameApiService.getData().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Responses>(){
                    override fun onSuccess(t: Responses) {

                        games.value = t.results

                        Log.i("ALDI", "asdasdasd.value.toString()")
                        gameDownloading.value = false
                        gameErrMsg.value = false
                    }

                    override fun onError(e: Throwable) {
                        gameDownloading.value = false
                        gameErrMsg.value = true
                        Log.i("HATA!!", gameErrMsg.value.toString())
                    }

                })
        )

    }
}