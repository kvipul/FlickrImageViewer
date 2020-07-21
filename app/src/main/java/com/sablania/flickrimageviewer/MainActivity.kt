package com.sablania.flickrimageviewer

import android.os.Bundle
import com.facebook.stetho.Stetho
import com.sablania.baseandroidlibrary.BaseActivity
import com.sablania.flickrimageviewer.databinding.ActivityMainBinding
import com.sablania.flickrimageviewer.fragments.FlickrImagesFragment

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //uncomment below line to enable stetho debug bridge
        Stetho.initializeWithDefaults(this)

        //After configuratio changes, fragment will already be there in the container
        if (supportFragmentManager.findFragmentByTag(FlickrImagesFragment.TAG) == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    binding.flContentFrame.id,
                    FlickrImagesFragment.newInstance(),
                    FlickrImagesFragment.TAG
                )
                .commit()
        }
    }
}