package com.m7.forecasto.base

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar
import com.m7.forecasto.R
import com.m7.forecasto.ui.LoadingDialog
import com.m7.forecasto.util.Logger
import com.m7.forecasto.util.Resource
import com.m7.forecasto.util.Status
import java.lang.IllegalStateException

abstract class BaseActivity<B : ViewDataBinding>(@LayoutRes private val layoutRes: Int) :
    AppCompatActivity() {

    lateinit var viewBinding: B

    var btnBack: Button? = null

    private val loadingDialog = LoadingDialog()

    abstract fun bindView(viewBinding: B)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<B>(this, layoutRes)
            .apply {
                viewBinding = this
                bindView(this)

                btnBack?.setOnClickListener {
                    onBackPressed()
                }
            }
    }

    fun <T> handleCall(
        resource: Resource<T>,
        onError: (() -> Unit)? = null,
        onSuccess: () -> Unit
    ) {
        when (resource.status) {
            Status.LOADING -> displayLoading()
            Status.SUCCESS -> {
                hideLoading()
                onSuccess.invoke()
            }
            Status.ERROR -> {
                hideLoading()
                displayError(resource.msg ?: getString(resource.resId ?: R.string.error_occurred))
                onError?.invoke()
            }
        }
    }

    fun startNext(targetActivity: Class<*>, data: Bundle? = null) {
        startActivity(Intent(this, targetActivity).apply {
            data?.apply { putExtras(this) }
        })
    }

    fun startNewTask(targetActivity: Class<*>, data: Bundle? = null) {
        startActivity(
            Intent(this, targetActivity).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                data?.let { putExtras(it) }
            }
        )
    }

    fun startNextForResult(targetActivity: Class<*>, code: Int, data: Bundle? = null) {
        startActivityForResult(
            Intent(
                this,
                targetActivity
            ).also { data?.apply { it.putExtras(this) } }, code
        )
    }

    fun displayMessage(msg: String?) {
        Logger.d("snack", "msg snake -> $msg")
        try {
            Snackbar.make(viewBinding.root, msg ?: "Done", Snackbar.LENGTH_LONG)
                .apply {
                    setBackgroundTint(getColor(R.color.purple_200))
                    setTextColor(Color.WHITE)
                }.show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this@BaseActivity, msg, Toast.LENGTH_LONG).show()
        }
    }

    fun displayError(errMsg: String = getString(R.string.error_occurred)) {
        Logger.d("snack", "err snake -> $errMsg")
        try {
            Snackbar.make(viewBinding.root, errMsg, Snackbar.LENGTH_LONG)
                .apply {
                    setBackgroundTint(Color.RED)
                    setTextColor(Color.WHITE)
                }.show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, errMsg, Toast.LENGTH_LONG).show()
        }
    }

    fun displayLoading() {
        Logger.d("loading", "display")
        supportFragmentManager.executePendingTransactions()
        if (!loadingDialog.isAdded)
            loadingDialog.show(supportFragmentManager, loadingDialog.TAG)
    }

    fun hideLoading() {
        Logger.d("loading", "hide")
        try {
            loadingDialog.dismiss()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }
}