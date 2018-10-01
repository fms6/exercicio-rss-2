package br.ufpe.cin.if710.rss

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import br.ufpe.cin.if710.rss.adapters.RssAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : Activity() {
    private var dynBroadcastReceiver: DynBroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicia o layout da recycler view
        conteudoRSS.layoutManager = LinearLayoutManager(this)
        conteudoRSS.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        dynBroadcastReceiver = DynBroadcastReceiver()

//         Para testar
//        doAsync {
//            database.deleteItems()
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.settings -> {
            // Quando o menú de configurações é ativado
            startActivity(Intent(applicationContext,PrefsMenuActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    override fun onResume() {
        super.onResume()
        active = true   // Valor acessado pelo RssLoadService e tem como finalidade informar se a activity está em primeiro plano

        // Cadastra o broadCastReceiver e a action que o aciona
        val intentFilter = IntentFilter(RssLoadService.ACTION_UPDATE_FEED)
        registerReceiver(dynBroadcastReceiver, intentFilter)

        // Recebe a shared Preference
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val rssFeed = prefs.getString(RSS_FEED, "default")

        // Inicializa um novo serviço para carregar o a lista de notícias
        val serviceIntent = Intent(this, RssLoadService::class.java)
        serviceIntent.putExtra("url", rssFeed)
        startService(serviceIntent)
    }


    override fun onPause(){
        super.onPause()
        unregisterReceiver(dynBroadcastReceiver)
        active = false
    }


    companion object {
        const val RSS_FEED = "rssfeed"
        var active = false
    }



    inner class DynBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            doAsync {
                val dbItemsRss = database.getItems()    // Recebe os ítens não lídos do banco
                uiThread {
                    conteudoRSS.adapter = RssAdapter(dbItemsRss, context)   // atualiza a lista de ítens
                }
            }
        }
    }


}
