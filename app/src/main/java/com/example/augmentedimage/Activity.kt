package com.example.augmentedimage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit

class  Activity : AppCompatActivity(R.layout.activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.commit {
            add(R.id.containerFragment, MainFragment::class.java, Bundle())
        }
    }
}