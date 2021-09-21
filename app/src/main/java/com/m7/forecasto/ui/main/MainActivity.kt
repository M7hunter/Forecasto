package com.m7.forecasto.ui.main

import androidx.activity.viewModels
import com.m7.forecasto.R
import com.m7.forecasto.base.BaseActivity
import com.m7.forecasto.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreation(viewBinding: ActivityMainBinding) {

    }
}