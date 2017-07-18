package com.zy.clipboard.ui

import android.Manifest
import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.zy.clipboard.R
import com.zy.clipboard.network.Network
import com.zy.clipboard.network.Server
import com.zy.clipboard.network.data.BASE_URL
import com.zy.clipboard.utils.permission
import com.zy.clipboard.utils.progress
import com.zy.clipboard.utils.toast
import com.zy.mocknet.Main
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_scan.*

class LoginActivity : AppCompatActivity(), QRCodeView.Delegate {

    lateinit var progress: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        permission(Manifest.permission.CAMERA)
        init()
    }

    fun init() {
        qrCodeView.startCamera()
        qrCodeView.showScanRect()
        qrCodeView.startSpot()
        qrCodeView.setResultHandler(this)
    }

    fun login(username: String?) {
        val retrofit = Network.getBaseRetrofit(BASE_URL)
        val server = retrofit.create(Server::class.java)
        server.login(username)
                .subscribeOn(Schedulers.newThread())
                .doOnSubscribe { progress = progress("login...") }
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    res ->
                    toast(res.toString())
                    loginSuccess()
                }, {
                    error ->
                    toast("error " + error.message)
                    loginFail()
                })
    }

    fun loginSuccess() {
        progress.dismiss()
        MainActivity.startMainActivity(this)
        finish()
    }

    fun loginFail() {
        progress.dismiss()
    }

    override fun onScanQRCodeSuccess(res: String?) {
        login(res)
        qrCodeView.startSpot()
    }

    override fun onScanQRCodeOpenCameraError() {
        toast("scan error")
        qrCodeView.startSpot()
    }

    override fun onDestroy() {
        super.onDestroy()
        qrCodeView.stopCamera()
        qrCodeView.hiddenScanRect()
        qrCodeView.stopSpot()
    }

}
