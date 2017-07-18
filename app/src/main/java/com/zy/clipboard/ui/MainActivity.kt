package com.zy.clipboard.ui

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zy.clipboard.R
import com.zy.clipboard.network.Network
import com.zy.clipboard.network.data.CopyMsg
import com.zy.clipboard.network.data.CopyMsgRes
import com.zy.clipboard.utils.Utils
import com.zy.clipboard.utils.toast
import com.zy.mocknet.MockNet
import com.zy.mocknet.application.MockConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_copy_msg.view.*

class MainActivity : AppCompatActivity() {

    lateinit var mockNet: MockNet
    lateinit var adapter: MsgAdapter
    val msgs = ArrayList<CopyMsg>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        initTest()
        init()
    }

    fun initTest() {
        var res = """
                 {"res": "success",
                  "msgs": [
                      {"content": "第三节课", "date": "2017/05/29 10:52:21"},
                      {"content": "第三节课", "date": "2017/05/29 10:51:55"},
                      {"content": "ddd", "date": "2017/05/29 10:51:50"},
                      {"content": "asdas", "date": "2017/05/29 10:51:40"},
                      {"content": "asdas", "date": "2017/05/29 10:46:45"},
                      {"content": "asdas", "date": "2017/05/29 10:39:38"},
                      {"content": "asdas", "date": "2017/05/29 10:38:59"}]}
                """
        mockNet = MockNet.create()
                .addConnection(
                        MockConnection.Builder()
                                .setMethod(MockConnection.GET)
                                .setUrl("/board/login")
                                .setResponseStatusCode(500)
                                .setResponseBody("text/json", "{res: success}")
                )
                .addConnection(
                        MockConnection.Builder()
                                .setMethod(MockConnection.GET)
                                .setUrl("/board/getCopyMsg")
                                .setResponseStatusCode(200)
                                .setResponseBody("text/json", res)
                )
                .start()
    }

    fun init() {
        refresh.setOnRefreshListener { getCopyMsg() }
        adapter = MsgAdapter(msgs)
        recycle.adapter = adapter
        recycle.layoutManager = LinearLayoutManager(this)
        getCopyMsg()
    }

    fun getCopyMsg() {
        refresh.isRefreshing = true
        var server = Network.getDefaultServer()
        server.getCopyMsg()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    res ->
                    getMsgSuccess(res)
                }, {
                    error ->
                    error.printStackTrace()
                    getMsgFail()
                })
    }

    fun getMsgSuccess(res: CopyMsgRes) {
        refresh.isRefreshing = false
        msgs.clear()
        msgs.addAll(res.msgs)
        adapter.notifyDataSetChanged()
    }

    fun getMsgFail() {
        toast("get copy msg error")
        refresh.isRefreshing = false
    }

    companion object {
        fun startMainActivity(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    inner class MsgAdapter(val msg: List<CopyMsg>): RecyclerView.Adapter<MsgAdapter.ViewHolder>() {

        override fun getItemCount(): Int = msg.size

        override fun onBindViewHolder(viewHolder: ViewHolder?, position: Int) {
            viewHolder?.itemView?.contentText?.text = msg[position].content
            viewHolder?.itemView?.setOnClickListener({
                Utils.copyToClipboard(this@MainActivity, msg[position].content)
            })
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            var view = LayoutInflater.from(parent?.context)
                    .inflate(R.layout.item_copy_msg, parent, false)
            return ViewHolder(view)
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }


}
