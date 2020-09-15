package com.example.placesearch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.store_list.view.*

class PlaceAdapter(var context: Context, var listStore : List<PlacesPojo.CustomA> , var models : List<StoreModel>) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
    class ViewHolder(itemView : View)  : RecyclerView.ViewHolder(itemView){
        fun setData(info: PlacesPojo.CustomA, storeModel: StoreModel) {
            itemView.txtStoreDist.setText(storeModel.distance.toString() + "\n" + storeModel.duration)
            itemView.txtStoreName.setText(info.name)
            itemView.txtStoreAddr.setText(info.vicinity)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.store_list,parent,false))
    }

    override fun getItemCount(): Int {
        return Math.min(5, listStore.size)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(listStore.get(holder.getAdapterPosition()), models.get(holder.getAdapterPosition()));
    }
}