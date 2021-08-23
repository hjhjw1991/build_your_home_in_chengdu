package com.hjhjw1991.barney.byh.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hjhjw1991.barney.byh.databinding.FragmentHomeBinding
import com.hjhjw1991.barney.byh.ui.BarneyWebView
import com.hjhjw1991.barney.byh.util.Logger
import com.hjhjw1991.barney.byh.util.ToastUtil
import com.hjhjw1991.barney.byh.util.removeSelfFromParent

/**
 * 主页
 */
class HomeFragment : Fragment() {
    init {
        Logger.log("init")
    }

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var contentView: BarneyWebView? = null
    var rootView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.log("onCreate HomeFragment")
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        if (rootView == null) {
            _binding = FragmentHomeBinding.inflate(inflater, container, false)
            // every time it creates a new view
            rootView = binding.root
            Logger.log("create $rootView")
        } else {
            rootView!!.removeSelfFromParent()
            Logger.log("reuse $rootView")
        }

        if (contentView == null) {
            val homeWeb: BarneyWebView = binding.homeWebview
            Logger.log("onCreateView $homeWeb")
            homeViewModel.url.observe(viewLifecycleOwner, Observer {
                context?.run { ToastUtil.show(this, "loading $it") }
                if (homeWeb.url != it) {
                    Logger.log("loading url=$it replace ${homeWeb.url}")
                    homeWeb.loadUrl(it)
                }
            })
            contentView = homeWeb
        } else if (contentView?.parent != null) {
            contentView!!.removeSelfFromParent()
            Logger.log("reuse $contentView")
        }
        return rootView
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        Logger.log("onDestroy HomeFragment")
        super.onDestroy()
    }
}