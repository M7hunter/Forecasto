package com.m7.forecasto.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7.forecasto.util.Resource
import com.m7.forecasto.util.handlers.ConnectivityHandler
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {

    @Inject
    lateinit var connectivityHandler: ConnectivityHandler

    fun <T> dbCall(observable: MutableLiveData<Resource<T>>, call: suspend () -> T) {
        viewModelScope.launch((CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            observable.postValue(Resource.error(msg = throwable.message.toString()))
        })) {
            observable.postValue(Resource.loading())

            call.invoke().apply {
                observable.postValue(Resource.success(this))
            }
        }
    }
}