package com.hjhjw1991.barney.byh.ui.buyhome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.amap.api.maps2d.MapsInitializer
import com.amap.api.services.core.ServiceSettings
import com.hjhjw1991.barney.loading.ui.BarneyLoadingView
import com.hjhjw1991.barney.byh.R
import com.hjhjw1991.barney.byh.databinding.FragmentBuyhomeBinding
import com.hjhjw1991.barney.byh.ui.BarneyWebView
import com.hjhjw1991.barney.util.Logger
import com.hjhjw1991.barney.util.removeSelfFromParent

/**
 * 购房tab
 */
class BuyHomeFragment : Fragment() {

    private lateinit var buyHomeViewModel: BuyHomeViewModel
    private var _binding: FragmentBuyhomeBinding? = null

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

        // todo 检查和请求定位权限
        ServiceSettings.updatePrivacyShow(context, true, true)
        ServiceSettings.updatePrivacyAgree(context, true)
        MapsInitializer.initialize(context)

        buyHomeViewModel =
            ViewModelProvider(this).get(BuyHomeViewModel::class.java)

        if (rootView == null) {
            _binding = FragmentBuyhomeBinding.inflate(inflater, container, false)
            // every time it creates a new view
            rootView = binding.root
            Logger.log("create $rootView")
        } else {
            rootView.removeSelfFromParent()
            Logger.log("reuse $rootView")
        }

        if (contentView == null) {
//            val innerWeb: BarneyWebView = binding.buyhomeWebview
//            Logger.log("onCreateView $innerWeb")
//            innerWeb.addWebChromeClient(object : WebChromeClient() {
//                val progress =
//                    (container?.parent as? ViewGroup)?.findViewById<BarneyLoadingView>(R.id.bar_percent_progress)
//
//                override fun onProgressChanged(view: WebView?, newProgress: Int) {
//                    super.onProgressChanged(view, newProgress)
//                    progress?.apply {
//                        setPercentage(newProgress.toFloat())
//                        if (newProgress >= 100) {
//                            visibility = View.GONE
//                        } else if (visibility == View.GONE) {
//                            visibility = View.VISIBLE
//                        }
//                    }
//                }
//            })
//            buyHomeViewModel.url.observe(viewLifecycleOwner) {
////                context?.run { ToastUtil.show(this, "loading $it") }
//                if (innerWeb.url != it) {
//                    Logger.log("loading url=$it replace ${innerWeb.url}")
//                    innerWeb.loadUrl(it)
//                }
//            }
//            contentView = innerWeb
        } else if (contentView?.parent != null) {
//            contentView.removeSelfFromParent()
//            Logger.log("reuse $contentView")
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