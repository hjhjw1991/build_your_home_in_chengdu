package com.hjhjw1991.barney.byh.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hjhjw1991.barney.byh.R
import com.hjhjw1991.barney.byh.databinding.FragmentHomeBinding
import com.hjhjw1991.barney.byh.databinding.FragmentNotificationsBinding
import com.hjhjw1991.barney.byh.ui.BarneyWebView
import com.hjhjw1991.barney.byh.ui.home.HomeViewModel
import com.hjhjw1991.barney.loading.ui.BarneyItem
import com.hjhjw1991.barney.loading.ui.BarneyLoadingView
import com.hjhjw1991.barney.loading.ui.BarneyPullRefreshLayout
import com.hjhjw1991.barney.util.Logger
import com.hjhjw1991.barney.util.removeSelfFromParent


/**
 * 消息页
 */
class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var contentView: RecyclerView? = null
    var rootView: View? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProvider(this).get(NotificationsViewModel::class.java)

        if (rootView == null) {
            _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
            // every time it creates a new view
            rootView = binding.root
            Logger.log("create $rootView")
        } else {
            rootView.removeSelfFromParent()
            Logger.log("reuse $rootView")
        }

        if (contentView == null) {
            contentView = binding.notificationList
            val adapter = MyAdapter(notificationsViewModel.notices)
            contentView?.adapter = adapter

            val refreshLayout: BarneyPullRefreshLayout = binding.notifications
            refreshLayout.setOnRefreshListener {
                notificationsViewModel.requestData()
                // do this in callback
                refreshLayout.isRefreshing = false
                refreshLayout.refresh(notificationsViewModel.notices.value!!)
                contentView?.adapter?.notifyDataSetChanged()
            }
            notificationsViewModel.notices.observe(viewLifecycleOwner, { newData ->
                contentView?.adapter?.notifyDataSetChanged()
            })
        } else if (contentView?.parent != null) {
            contentView.removeSelfFromParent()
            Logger.log("reuse $contentView")
        }
        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private class MyAdapter(listData: LiveData<List<BarneyItem>>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val data = listData.value!!
    override fun getItemViewType(position: Int): Int {
        return TYPE_CONTENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notification, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as MyViewHolder
        viewHolder.titleView.text = "第" + position + "行"
        viewHolder.contentView.text = data[position].toString()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    companion object {
        private const val TYPE_CONTENT = 0 //正常内容
        private const val TYPE_FOOTER = 1 //下拉刷新
    }
}

private class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val titleView: TextView = itemView.findViewById(R.id.text_notification_title)
    val contentView: TextView = itemView.findViewById(R.id.text_notification_content)
}