package com.hjhjw1991.barney.byh.ui.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hjhjw1991.barney.byh.R
import com.hjhjw1991.barney.byh.databinding.FragmentMessageBinding
import com.hjhjw1991.barney.loading.ui.BarneyItem
import com.hjhjw1991.barney.loading.ui.BarneyPullRefreshLayout
import com.hjhjw1991.barney.util.Logger
import com.hjhjw1991.barney.util.removeSelfFromParent
import java.util.*


/**
 * 消息tab
 */
class MessageFragment : Fragment() {

    private lateinit var messageViewModel: MessageViewModel
    private var _binding: FragmentMessageBinding? = null

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
        messageViewModel =
                ViewModelProvider(this).get(MessageViewModel::class.java)

        if (rootView == null) {
            _binding = FragmentMessageBinding.inflate(inflater, container, false)
            // every time it creates a new view
            rootView = binding.root
            Logger.log("create $rootView")
        } else {
            rootView.removeSelfFromParent()
            Logger.log("reuse $rootView")
        }

        if (contentView == null) {
            contentView = binding.messageList
            val adapter = MyAdapter(messageViewModel.notices.value!!)
            contentView?.layoutManager = LinearLayoutManager(context)
            contentView?.adapter = adapter

            val refreshLayout: BarneyPullRefreshLayout = binding.messages
            refreshLayout.setOnRefreshListener {
                messageViewModel.requestData()
                // do this in callback
                refreshLayout.isRefreshing = false
            }
            messageViewModel.notices.observe(viewLifecycleOwner) { newData ->
                adapter.updateData(newData)
                adapter.notifyDataSetChanged()
            }
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

private class MyAdapter(private var data: List<BarneyItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun updateData(newData: List<BarneyItem>) {
        this.data = mutableListOf<BarneyItem>().apply {
            addAll(newData)
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_CONTENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notice, parent, false)
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
    val titleView: TextView = itemView.findViewById(R.id.text_message_title)
    val contentView: TextView = itemView.findViewById(R.id.text_message_content)
}