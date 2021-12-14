package com.bostonicc.org.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bostonicc.org.fragments.HomeFragment

class HomeViewPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragmentHashMap: HashMap<Int, Fragment> = HashMap()

    override fun getItem(position: Int): Fragment {

        fragmentHashMap[position]?.let {
            return it
        }

        //home    https://www.bostonicc.org/
        //connect    https://www.bostonicc.org/rsvp/
        //donate    https://www.paypal.com/donate/?cmd=_s-xclick&hosted_button_id=BRQYX6TRLYHL8
        //media    https://www.bostonicc.org/sermons/
        //ministries    https://www.bostonicc.org/ministries/

        when (position) {
            0 -> {
                val args = Bundle()
                args.putString("url" , "https://www.bostonicc.org/")
                val homeFragment = HomeFragment()
                homeFragment.arguments = args
                fragmentHashMap[position] = homeFragment
                return homeFragment
            }
            1 -> {
                val args = Bundle()
                args.putString("url" , "https://www.bostonicc.org/rsvp/")
                val feedFragment = HomeFragment()
                feedFragment.arguments = args
                fragmentHashMap[position] = feedFragment
                return feedFragment
            }
            2 -> {
                val args = Bundle()
                args.putString("url" , "https://www.paypal.com/donate/?cmd=_s-xclick&hosted_button_id=BRQYX6TRLYHL8")
                val profileFragment = HomeFragment()
                profileFragment.arguments = args
                fragmentHashMap[position] = profileFragment
                return profileFragment
            }
            3 -> {
                val args = Bundle()
                args.putString("url" , "https://www.bostonicc.org/sermons/")
                val notificationFragment = HomeFragment()
                notificationFragment.arguments = args
                fragmentHashMap[position] = notificationFragment
                return notificationFragment
            }
            4 -> {
                val args = Bundle()
                args.putString("url" , "https://www.bostonicc.org/ministries/")
                val messageFragment = HomeFragment()
                messageFragment.arguments = args
                fragmentHashMap[position] = messageFragment
                return messageFragment
            }
        }
        val homeFragment = HomeFragment()
        fragmentHashMap[position] = homeFragment
        return homeFragment
    }

    override fun getCount(): Int {
        return 5
    }

}