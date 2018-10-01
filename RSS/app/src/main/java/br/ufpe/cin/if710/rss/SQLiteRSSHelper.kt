package br.ufpe.cin.if710.rss

import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import org.jetbrains.anko.db.*


class SQLiteRSSHelper(ctx: Context): ManagedSQLiteOpenHelper(ctx, DATABASE_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        //Executa o comando de criação de tabela
        db.execSQL(CREATE_DB_COMMAND)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //estamos ignorando esta possibilidade no momento
        throw RuntimeException("nao se aplica")
    }

    //IMPLEMENTAR ABAIXO
    //Implemente a manipulação de dados nos métodos auxiliares para não ficar criando consultas manualmente
    fun insertItem(item: ItemRSS): Long {
        return insertItem(item.title, item.pubDate, item.description, item.link)
    }

    fun insertItem(title: String, pubDate: String, description: String, link: String): Long {
        return writableDatabase
                .insert(DATABASE_TABLE,
                ITEM_TITLE to title,
                ITEM_DATE to pubDate,
                ITEM_DESC to description,
                ITEM_LINK to link,
                ITEM_UNREAD to true)
    }

    @Throws(SQLException::class)
    fun getItemRSS(link: String): ItemRSS? {
        return readableDatabase
                .select(DATABASE_TABLE, ITEM_TITLE, ITEM_DESC, ITEM_DATE, ITEM_LINK)
                .whereArgs("$ITEM_LINK = \"$link\"")
                .parseOpt(classParser())
    }

    fun markAsUnread(link: String): Boolean {
        writableDatabase.update(DATABASE_TABLE, ITEM_UNREAD to true)
                .whereSimple("$ITEM_LINK = \"$link\"")
                .exec()
        return false
    }

    fun markAsRead(link: String): Boolean {
        writableDatabase.update(DATABASE_TABLE, ITEM_UNREAD to false)
                .whereSimple("$ITEM_LINK = \"$link\"")
                .exec()
        return false
    }

    fun getItems(): List<ItemRSS> {
        return readableDatabase
                .select(DATABASE_TABLE, ITEM_TITLE, ITEM_LINK, ITEM_DATE, ITEM_DESC)
                .whereSimple("$ITEM_UNREAD = 1")
                .parseList(classParser())
    }

    fun deleteItems() {
        writableDatabase.delete(DATABASE_TABLE)
    }

    companion object {
        //Nome do Banco de Dados
        private val DATABASE_NAME = "rss"
        //Nome da tabela do Banco a ser usada
        val DATABASE_TABLE = "items"
        //Versão atual do banco
        private val DB_VERSION = 1

        private var instance: SQLiteRSSHelper? = null

        //Definindo Singleton
        @Synchronized
        fun getInstance(ctx: Context): SQLiteRSSHelper {
            if (instance == null) {
                instance = SQLiteRSSHelper(ctx.applicationContext)
            }
            return instance!!
        }

        //Definindo constantes que representam os campos do banco de dados
        val ITEM_ROWID = RssProviderContract._ID
        val ITEM_TITLE = RssProviderContract.TITLE
        val ITEM_DATE = RssProviderContract.DATE
        val ITEM_DESC = RssProviderContract.DESCRIPTION
        val ITEM_LINK = RssProviderContract.LINK
        val ITEM_UNREAD = RssProviderContract.UNREAD

        //Definindo constante que representa um array com todos os campos
        val columns = arrayOf(ITEM_ROWID, ITEM_TITLE, ITEM_DATE, ITEM_DESC, ITEM_LINK, ITEM_UNREAD)

        //Definindo constante que representa o comando de criação da tabela no banco de dados
        private val CREATE_DB_COMMAND = "CREATE TABLE " + DATABASE_TABLE + " (" +
                ITEM_ROWID + " integer primary key autoincrement, " +
                ITEM_TITLE + " text not null, " +
                ITEM_DATE + " text not null, " +
                ITEM_DESC + " text not null, " +
                ITEM_LINK + " text not null, " +
                ITEM_UNREAD + " boolean not null);"
    }

}

// Access property for Context
val Context.database: SQLiteRSSHelper
    get() = SQLiteRSSHelper.getInstance(applicationContext)