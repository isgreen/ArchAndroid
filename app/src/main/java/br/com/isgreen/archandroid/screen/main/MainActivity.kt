package br.com.isgreen.archandroid.screen.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.isgreen.archandroid.R
import br.com.isgreen.archandroid.base.BaseActivity

/**
 * Created by Éverdes Soares on 08/07/2019.
 */

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}