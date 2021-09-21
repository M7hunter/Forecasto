package com.m7.forecasto.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7.forecasto.R
import com.m7.forecasto.util.Resource
import com.m7.forecasto.util.handlers.ConnectivityHandler
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {

    @Inject
    lateinit var connectivityHandler: ConnectivityHandler

    fun <T> call(observer: MutableLiveData<Resource<T>>, call: suspend () -> Response<T>) {
        viewModelScope.launch((CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            observer.postValue(Resource.error(msg = throwable.message.toString()))
        })) {
            if (connectivityHandler.isConnected()) {
                observer.postValue(Resource.loading())

                call.invoke().apply {
                    if (isSuccessful) {
                        observer.postValue(Resource.success(body()))
                    } else {
                        observer.postValue(Resource.error(resId = R.string.not_successful))
                    }
                }
            } else {
                observer.postValue(Resource.error(resId = R.string.no_internet_connection))
            }
        }
    }
}