package com.blockdev.birdy.adaper

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.blockdev.birdy.R
import com.blockdev.birdy.model.Message
import com.blockdev.birdy.model.MyFirebase.myUid

class ChatAdapter(private val context: Context, private val chatMessages: List<Message>) :
    BaseAdapter() {
    override fun getCount(): Int {
        return chatMessages.size
    }

    override fun getItem(position: Int): Message {
        return chatMessages.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.viewholder_message, parent, false)
            holder = createViewHolder(convertView)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val chatMessage = getItem(position)
        setAlignment(holder, chatMessage.senderUid)
        holder.message!!.text = chatMessage.message
        return convertView
    }

    private fun setAlignment(holder: ViewHolder, senderUid: String?) {
        if (senderUid != myUid) {
            holder.background!!.setBackgroundResource(R.drawable.chat_u_bg)
            holder.message!!.setTextColor(Color.BLACK)
            val lp = holder.container!!.layoutParams as RelativeLayout.LayoutParams
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0)
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            holder.container!!.layoutParams = lp
        } else {
            holder.background!!.setBackgroundResource(R.drawable.chat_me_bg)
            holder.message!!.setTextColor(Color.WHITE)
            val lp = holder.container!!.layoutParams as RelativeLayout.LayoutParams
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0)
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            holder.container!!.layoutParams = lp
        }
    }

    private fun createViewHolder(v: View): ViewHolder {
        val holder = ViewHolder()
        holder.message = v.findViewById<View>(R.id.vh_msg_txt) as TextView
        holder.container = v.findViewById<View>(R.id.vh_msg_container) as LinearLayout
        holder.background = v.findViewById<View>(R.id.vh_msg_bg) as LinearLayout
        holder.layout = v.findViewById<View>(R.id.vh_msg_layout) as RelativeLayout
        holder.timestamp = v.findViewById<View>(R.id.vh_msg_timestamp) as TextView
        return holder
    }

    private class ViewHolder {
        var layout: RelativeLayout? = null
        var message: TextView? = null
        var timestamp: TextView? = null
        var container: LinearLayout? = null
        var background: LinearLayout? = null
    }
}
