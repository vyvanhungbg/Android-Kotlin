package com.atom.android.muvik


import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.atom.android.muvik.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationBarView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController =  Navigation.findNavController(this, R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
       /* val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )*/
        setupActionBarWithNavController(navController)

        navView.setupWithNavController(navController)

        val view: View = binding.designBottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(view)



        val navHeight:Float   by lazy { navView.height.toFloat()}
        bottomSheetBehavior.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback(){
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_COLLAPSED -> {


                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {

                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    Log.e("slide off", slideOffset.toString())
                    Log.e("nav ", navHeight.toString())

                    // Start the animation
                    if(slideOffset >=0)
                    navView.animate()
                        .translationY(navHeight * slideOffset)
                        .alpha(1.0f)
                        .setDuration(0)
                        .setListener(null);
                }

            }
        )


      /*  binding.bottomsheetText.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
            }
        }*/






       navView.setOnItemSelectedListener { item ->
           when (item.itemId) {
               R.id.navigation_notifications -> {
                   bottomSheetBehavior.isHideable = true
                   bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                   NavigationUI.onNavDestinationSelected(item, navController)
               }
               else -> {
                   bottomSheetBehavior.isHideable = false
                   bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                   NavigationUI.onNavDestinationSelected(item, navController)
               }

           }
       }



       /* binding.navView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener {
            item: MenuItem ->

        })*/




    }
}