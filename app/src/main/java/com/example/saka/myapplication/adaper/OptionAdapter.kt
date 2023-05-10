package com.example.saka.myapplication.adaper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.saka.myapplication.R

/**
 * Created by Saka on 04-Jun-17.
 */
class OptionAdapter(
    private var options: Array<String>,
    private var icons: IntArray
) : RecyclerView.Adapter<OptionAdapter.OptionHolder>() {

    inner class OptionHolder(view: View) : RecyclerView.ViewHolder(view) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionHolder {
        val rowView =
            LayoutInflater.from(parent.context).inflate(R.layout.viewholder_option, parent, false)
        return OptionHolder(rowView)
    }

    override fun getItemCount(): Int {
        return options.size
    }

    override fun onBindViewHolder(holder: OptionHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.vh_option_txt).text = options[position]
        holder.itemView.findViewById<ImageView>(R.id.vh_option_img)
            .setImageResource(icons[position])
    }
}
