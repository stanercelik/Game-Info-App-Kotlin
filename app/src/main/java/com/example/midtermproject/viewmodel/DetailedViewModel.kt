package com.example.midtermproject.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.midtermproject.model.GameClass
import com.example.midtermproject.service.GameDescAPIService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

/* ViewModel for the DescFragment Class */
class DetailedViewModel : ViewModel() {
    val detGameLiveData = MutableLiveData<GameClass?>()
    val gameErrMsg = MutableLiveData<Boolean>()
    val gameDownloading = MutableLiveData<Boolean>()

    private val detGameApiService = GameDescAPIService()
    private val disposable = CompositeDisposable()




    fun getData(query: String) {
        getDetDataFromInternet(query)
    }

    fun getDetDataFromInternet(query: String) {
        gameDownloading.value = true
        disposable.add(
            detGameApiService.getDetData(query).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<GameClass>() {
                    override fun onSuccess(t: GameClass) {

                        detGameLiveData.value = t

                        Log.i("ALDI", "ALDI")
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