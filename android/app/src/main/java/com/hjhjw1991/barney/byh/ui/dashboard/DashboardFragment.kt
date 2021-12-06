package com.hjhjw1991.barney.byh.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hjhjw1991.barney.ui.BarneyLoadingView
import com.hjhjw1991.barney.byh.R
import com.hjhjw1991.barney.byh.databinding.FragmentDashboardBinding
import com.hjhjw1991.barney.byh.ui.BarneyWebView
import com.hjhjw1991.barney.util.Logger
import com.hjhjw1991.barney.util.removeSelfFromParent

/**
 * 导航页
 */
class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var contentView: BarneyWebView? = null
    var rootView: View? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)

        if (rootView == null) {
            _binding = FragmentDashboardBinding.inflate(inflater, container, false)
            // every time it creates a new view
            rootView = binding.root
            Logger.log("create $rootView")
        } else {
            rootView!!.removeSelfFromParent()
            Logger.log("reuse $rootView")
        }

        if (contentView == null) {
            val innerWeb: BarneyWebView = binding.dashboardWebview
            Logger.log("onCreateView $innerWeb")
            innerWeb.addWebChromeClient(object : WebChromeClient() {
                val progress =
                    (container?.parent as? ViewGroup)?.findViewById<BarneyLoadingView>(R.id.bar_percent_progress)

                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    progress?.setPercentage(newProgress.toFloat())
                    if (newProgress >= 100) {
                        progress?.visibility = View.GONE
                    } else if (progress?.visibility == View.GONE) {
                        progress?.visibility = View.VISIBLE
                    }
                }
            })
            dashboardViewModel.url.observe(viewLifecycleOwner, {
//                context?.run { ToastUtil.show(this, "loading $it") }
                if (innerWeb.url != it) {
                    Logger.log("loading url=$it replace ${innerWeb.url}")
                    innerWeb.loadUrl(it)
                }
            })
            contentView = innerWeb
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
        Logger.log("onDestroy DashboardFragment")
        super.onDestroy()
    }
}