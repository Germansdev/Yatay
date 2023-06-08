package com.islayatay

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.islayatay.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    //luego de binding que daba error, sacando binding y esto, daba ok
    //private lateinit var binding:ActivityMainBinding
    private lateinit var navController: NavController



    override fun onCreate(savedInstanceState: Bundle?) {

        //Thread.sleep(800)

        setTheme(R.style.Theme_IslaYatay)


        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Retrieve NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Set up the action bar for use with the NavController
        setupActionBarWithNavController(navController)

    }



   /** override fun onSaveInstanceState(outState: Bundle) {
        // Save the user's current game state
        outState?.run {
            putInt(EMAIL,  )

        }

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState)
    }

    companion object {
        val EMAIL = "email"

    }
   */


        override fun onSupportNavigateUp(): Boolean {
            return navController.navigateUp() || super.onSupportNavigateUp()
        }




            /**val miIntent = Intent(this,
            HomeFragment::class.java)
        startActivity(miIntent)*/

}