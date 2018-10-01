package br.ufpe.cin.if710.rss

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_web.*
import android.webkit.WebViewClient
import org.jetbrains.anko.doAsync

// Activity para exibir a página web

class WebActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        webview.webViewClient = WebViewClient()

        val url: String = intent.getStringExtra(INTENT_RSS_LINK)
                ?: throw IllegalStateException("field $INTENT_RSS_LINK missing in Intent")

        // Se tudo correu bem, registra no banco que a url já foi lida
        doAsync{
            database.markAsRead(url)
        }
        webview.loadUrl(url)

    }

    // Criação de intent para chamar a WebActivity
    companion object {

        private const val INTENT_RSS_LINK = "rss_link"

        fun newIntent(context: Context, itemRSS: ItemRSS): Intent {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra(INTENT_RSS_LINK, itemRSS.link)
            return intent
        }
    }
}