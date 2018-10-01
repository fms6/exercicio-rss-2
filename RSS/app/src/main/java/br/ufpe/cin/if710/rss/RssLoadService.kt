package br.ufpe.cin.if710.rss

import android.app.IntentService
import android.content.Intent
import android.util.Log
import br.ufpe.cin.if710.rss.MainActivity.Companion.active
import java.io.IOException
import java.net.URL

class RssLoadService : IntentService("RssLoadService") {
    override fun onHandleIntent(intent: Intent?) {
        val TAG = this::class.java.simpleName
        val url = intent?.getStringExtra("url")

        var result: String = ""
        try {
            val result = URL(url).readText()   // carrega texto da internet
            val newItemsRss = ParserRSS.parse(result)  // faz o parsing do texto
            var inserted = 0
            for (itemRss in newItemsRss) {
                if (database.getItemRSS(itemRss.link) == null) {
                    database.insertItem(itemRss)    // Realiza persistência dos dados, caso não existam
                    inserted++                      // Incrementa a cada novo item inserido
                }
            }

            sendBroadcast(Intent(ACTION_UPDATE_FEED))   // Notifica que o feed foi atualizado
            if(!active && inserted > 0) // Notifica apenas se a tela não está ativa e se existem novas notícias
                sendBroadcast(Intent(this, StaticBroadcastReceiver::class.java))
        } catch (e: IOException){
            Log.e(TAG, e.toString())
        }



    }

    companion object {
        const val ACTION_UPDATE_FEED = "br.ufpe.cin.if710.rss.UPDATE_RSS_FEED"
    }

}