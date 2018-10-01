package br.ufpe.cin.if710.rss.adapters

import android.content.Context

import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ufpe.cin.if710.rss.ItemRSS
import br.ufpe.cin.if710.rss.R
import br.ufpe.cin.if710.rss.WebActivity
import kotlinx.android.synthetic.main.itemlista.view.*

class RssAdapter(private val rssList: List<ItemRSS>, private val c: Context)
    : RecyclerView.Adapter<RssAdapter.ViewHolder>(){
    override fun getItemCount(): Int = rssList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // gerando um novo elemento para interface gráfica
        val view = LayoutInflater.from(c).inflate(R.layout.itemlista, parent, false)
        return ViewHolder(view)
    }

    // Ao clicar em um ítem, gera um intent implícito para poder ir pra outra activity
    private val onClickListener: (ItemRSS) -> Unit = { itemRSS->
        val intent = WebActivity.newIntent(c, itemRSS)
        c.startActivity(intent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = rssList[position]

        holder.bind(item, onClickListener)
    }

    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        // Configura os widgets da view e o onClickListener
        fun bind(itemRSS: ItemRSS, clickListener: (ItemRSS) -> Unit) {
            itemView.item_titulo.text = itemRSS.title
            itemView.item_data.text = itemRSS.pubDate
            itemView.setOnClickListener { clickListener(itemRSS)}
        }
    }
}