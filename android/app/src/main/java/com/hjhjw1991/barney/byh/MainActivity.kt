package com.hjhjw1991.barney.byh

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphNavigator
import androidx.navigation.NavigatorProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hjhjw1991.barney.byh.databinding.ActivityMainBinding
import com.hjhjw1991.barney.byh.nav.FixFragmentNavigator
import com.hjhjw1991.barney.byh.ui.dashboard.DashboardFragment
import com.hjhjw1991.barney.byh.ui.home.HomeFragment
import com.hjhjw1991.barney.byh.ui.notifications.NotificationsFragment
import com.hjhjw1991.barney.serviceprovider.ServiceManager

/**
 * 主入口
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // get host fragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)!!
        // use custom navigator
        navController.navigatorProvider.addNavigator(
            FixFragmentNavigator(
                this,
                navHostFragment.childFragmentManager,
                R.id.nav_host_fragment_activity_main
            )
        )
        // use custom graph
        navController.setGraph(R.navigation.fix_nav_graph)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        ServiceManager.getService(IDemoService::class.java)?.sayHello(this)
    }

    //手动创建导航图，把3个目的地添加进来
    private fun initNavGraph(
        provider: NavigatorProvider,
        fragmentNavigator: FixFragmentNavigator
    ): NavGraph {
        val navGraph = NavGraph(NavGraphNavigator(provider))

        //用自定义的导航器来创建目的地
        val destination1 = fragmentNavigator.createDestination()
        destination1.id = R.id.navigation_home
        destination1.className = HomeFragment::class.java.canonicalName.orEmpty()
        destination1.label = resources.getString(R.string.title_home)
        navGraph.addDestination(destination1)

        val destination2 = fragmentNavigator.createDestination()
        destination2.id = R.id.navigation_dashboard
        destination2.className = DashboardFragment::class.java.canonicalName.orEmpty()
        destination2.label = resources.getString(R.string.title_dashboard)
        navGraph.addDestination(destination2)

        val destination3 = fragmentNavigator.createDestination()
        destination3.id = R.id.navigation_notifications
        destination3.className = NotificationsFragment::class.java.canonicalName.orEmpty()
        destination3.label = resources.getString(R.string.title_notifications)
        navGraph.addDestination(destination3)

        navGraph.startDestination = R.id.navigation_home
        return navGraph
    }
}