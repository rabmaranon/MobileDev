package com.bostonicc.org.activity

import android.Manifest
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bostonicc.org.databinding.ActivityMainBinding
import com.bostonicc.org.databinding.TabHomeBinding
import com.bostonicc.org.adapter.HomeViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager

import android.os.Build
import android.util.Log
import com.bostonicc.org.R


class MainActivity : AppCompatActivity() {


    private val TAG = "MainActivity"
    lateinit var binding: ActivityMainBinding

    lateinit var profileViewpagerBinding: TabHomeBinding
    lateinit var customTabFeedViewpagerBinding: TabHomeBinding
    lateinit var customTabNotificationsViewpagerBinding: TabHomeBinding


    //home    https://www.bostonicc.org/
    //connect    https://www.bostonicc.org/rsvp/
    //donate    https://www.paypal.com/donate/?cmd=_s-xclick&hosted_button_id=BRQYX6TRLYHL8
    //media    https://www.bostonicc.org/sermons/
    //ministries    https://www.bostonicc.org/ministries/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (Build.VERSION.SDK_INT>=23){
            isStoragePermissionGranted();
        }

        binding.viewPager.adapter = HomeViewPagerAdapter(supportFragmentManager)
        binding.tlBottomNavigation.setupWithViewPager(binding.viewPager)

        val layout =
            (binding.tlBottomNavigation.getChildAt(0) as LinearLayout).getChildAt(0) as LinearLayout
        val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
        layout.layoutParams = layoutParams


        binding.viewPager.offscreenPageLimit = 5

        val tabHomeBinding: TabHomeBinding = DataBindingUtil.inflate(
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
            R.layout.tab_home, null, false
        )

        tabHomeBinding.ivIcon.setImageResource(R.drawable.ic_home)
        tabHomeBinding.ivIcon.setColorFilter(
            ContextCompat.getColor(
                this@MainActivity,
                R.color.white
            ), android.graphics.PorterDuff.Mode.SRC_IN
        )

        tabHomeBinding.tvText.text = "Home"
        tabHomeBinding.tvText.setTextColor(
            ContextCompat.getColor(
                this@MainActivity,
                R.color.white
            )
        )

        binding.tlBottomNavigation.getTabAt(0)?.customView = tabHomeBinding.root


        //2


        customTabFeedViewpagerBinding = DataBindingUtil.inflate(
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
            R.layout.tab_home, null, false
        )

        customTabFeedViewpagerBinding.ivIcon.setImageResource(R.drawable.connect)
        customTabFeedViewpagerBinding.ivIcon.setColorFilter(
            ContextCompat.getColor(
                this@MainActivity,
                R.color.dark_grey
            )
        )

        customTabFeedViewpagerBinding.tvText.text = "Connect"
        binding.tlBottomNavigation.getTabAt(1)?.customView = customTabFeedViewpagerBinding.root

        profileViewpagerBinding = DataBindingUtil.inflate(
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
            R.layout.tab_home, null, false
        )

        profileViewpagerBinding.ivIcon.setImageResource(R.drawable.donate)
        profileViewpagerBinding.ivIcon.setColorFilter(
            ContextCompat.getColor(
                this@MainActivity,
                R.color.dark_grey
            )
        )
        profileViewpagerBinding.tvText.text = "Give"


        binding.tlBottomNavigation.getTabAt(2)?.customView = profileViewpagerBinding.root

        customTabNotificationsViewpagerBinding = DataBindingUtil.inflate(
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
            R.layout.tab_home, null, false
        )

        customTabNotificationsViewpagerBinding.ivIcon.setImageResource(R.drawable.media)
        customTabNotificationsViewpagerBinding.ivIcon.setColorFilter(
            ContextCompat.getColor(
                this@MainActivity,
                R.color.dark_grey
            )
        )
        customTabNotificationsViewpagerBinding.tvText.text =
            "Media"
        binding.tlBottomNavigation.getTabAt(3)?.customView =
            customTabNotificationsViewpagerBinding.root


        val tabMessageBinding: TabHomeBinding = DataBindingUtil.inflate(
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
            R.layout.tab_home, null, false
        )

        tabMessageBinding.tvText.text = "Events"
        tabMessageBinding.ivIcon.setImageResource(R.drawable.event)
        tabMessageBinding.ivIcon.setColorFilter(
            ContextCompat
                .getColor(this@MainActivity, R.color.dark_grey),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        binding.tlBottomNavigation.getTabAt(4)?.customView = tabMessageBinding.root


        val customTabMoreViewpagerBinding: TabHomeBinding = DataBindingUtil.inflate(
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
            R.layout.tab_home, null, false
        )

        customTabMoreViewpagerBinding.ivIcon.setImageResource(R.drawable.ic_home)
        customTabMoreViewpagerBinding.ivIcon.setColorFilter(
            ContextCompat.getColor(
                this@MainActivity,
                R.color.dark_grey
            ), android.graphics.PorterDuff.Mode.SRC_IN
        )
        customTabMoreViewpagerBinding.tvText.text = "Home"
        binding.tlBottomNavigation.getTabAt(5)?.customView = customTabMoreViewpagerBinding.root

        binding.tlBottomNavigation.addOnTabSelectedListener(
            object : TabLayout.ViewPagerOnTabSelectedListener(binding.viewPager) {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    super.onTabSelected(tab)

                    when (tab.position) {
                        0 -> {
                            tabHomeBinding.tvText.setTextColor(
                                ContextCompat.getColor(
                                    this@MainActivity,
                                    R.color.white
                                )
                            )
                            tabHomeBinding.ivIcon.setColorFilter(
                                ContextCompat.getColor(
                                    this@MainActivity,
                                    R.color.white
                                ), android.graphics.PorterDuff.Mode.SRC_IN
                            )
                        }
                        1 -> {
                            customTabFeedViewpagerBinding.tvText.setTextColor(
                                ContextCompat.getColor(
                                    this@MainActivity,
                                    R.color.white
                                )
                            )
                            customTabFeedViewpagerBinding.ivIcon.setColorFilter(
                                ContextCompat.getColor(
                                    this@MainActivity,
                                    R.color.white
                                ), android.graphics.PorterDuff.Mode.SRC_IN
                            )


                        }
                        2 -> {
                            profileViewpagerBinding.tvText.setTextColor(
                                ContextCompat.getColor(
                                    this@MainActivity,
                                    R.color.white
                                )
                            )
                            profileViewpagerBinding.ivIcon.setColorFilter(
                                ContextCompat.getColor(
                                    this@MainActivity,
                                    R.color.white
                                ), android.graphics.PorterDuff.Mode.SRC_IN
                            )


                        }
                        3 -> {
                            customTabNotificationsViewpagerBinding.tvText.setTextColor(
                                ContextCompat.getColor(
                                    this@MainActivity,
                                    R.color.white
                                )
                            )
                            customTabNotificationsViewpagerBinding.ivIcon.setColorFilter(
                                ContextCompat.getColor(
                                    this@MainActivity,
                                    R.color.white
                                ), android.graphics.PorterDuff.Mode.SRC_IN
                            )
                        }
                        4 -> {
                            tabMessageBinding.tvText.setTextColor(
                                ContextCompat.getColor(
                                    this@MainActivity,
                                    R.color.white
                                )
                            )
                            tabMessageBinding.ivIcon.setColorFilter(
                                ContextCompat.getColor(
                                    this@MainActivity,
                                    R.color.white
                                ), android.graphics.PorterDuff.Mode.SRC_IN
                            )
                        }

                    }

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    super.onTabUnselected(tab)
                    when (tab?.position) {
                        0 -> {
                            tabHomeBinding.tvText.setTextColor(
                                ContextCompat.getColor(
                                    this@MainActivity,
                                    R.color.dark_grey
                                )
                            )
                            tabHomeBinding.ivIcon.setColorFilter(
                                ContextCompat.getColor(
                                    this@MainActivity,
                                    R.color.dark_grey
                                ), android.graphics.PorterDuff.Mode.SRC_IN
                            )
                        }
                        1 -> {
                            customTabFeedViewpagerBinding.tvText.setTextColor(
                                ContextCompat.getColor(
                                    this@MainActivity,
                                    R.color.dark_grey
                                )
                            )
                            customTabFeedViewpagerBinding.ivIcon.setColorFilter(
                                ContextCompat.getColor(
                                    this@MainActivity,
                                    R.color.dark_grey
                                ), android.graphics.PorterDuff.Mode.SRC_IN
                            )
                        }
                        2 -> {
                            profileViewpagerBinding.tvText.setTextColor(
                                ContextCompat.getColor(
                                    this@MainActivity,
                                    R.color.dark_grey
                                )
                            )
                            profileViewpagerBinding.ivIcon.setColorFilter(
                                ContextCompat.getColor(
                                    this@MainActivity,
                                    R.color.dark_grey
                                ), android.graphics.PorterDuff.Mode.SRC_IN
                            )
                        }
                        3 -> {
                            customTabNotificationsViewpagerBinding.tvText.setTextColor(
                                ContextCompat.getColor(
                                    this@MainActivity,
                                    R.color.dark_grey
                                )
                            )
                            customTabNotificationsViewpagerBinding.ivIcon.setColorFilter(
                                ContextCompat.getColor(
                                    this@MainActivity,
                                    R.color.dark_grey
                                ), android.graphics.PorterDuff.Mode.SRC_IN
                            )
                        }
                        4 -> {
                            tabMessageBinding.tvText.setTextColor(
                                ContextCompat.getColor(
                                    this@MainActivity,
                                    R.color.dark_grey
                                )
                            )
                            tabMessageBinding.ivIcon.setColorFilter(
                                ContextCompat.getColor(
                                    this@MainActivity,
                                    R.color.dark_grey
                                ), android.graphics.PorterDuff.Mode.SRC_IN
                            )
                        }

                    }


                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    super.onTabReselected(tab)

                }
            }
        )

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }


    fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v(TAG, "Permission is granted")
                true
            } else {
                Log.v(TAG, "Permission is revoked")
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ), 1
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted")
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            0 -> {
                var isPerpermissionForAllGranted = false
                if (grantResults.size > 0 && permissions.size == grantResults.size) {
                    var i = 0
                    while (i < permissions.size) {
                        isPerpermissionForAllGranted =
                            grantResults[i] == PackageManager.PERMISSION_GRANTED
                        i++
                    }
                    Log.e("value", "Permission Granted, Now you can use local drive .")
                } else {
                    isPerpermissionForAllGranted = true
                    Log.e("value", "Permission Denied, You cannot use the app")
                    isStoragePermissionGranted()
                }
                if (isPerpermissionForAllGranted) {
                }
            }
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }
}