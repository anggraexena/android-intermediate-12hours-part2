package com.example.gungde.intermediate12hours_2

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.gungde.intermediate12hours_2.model.Base
import com.example.gungde.intermediate12hours_2.tools.ProgressDialogManager
import com.reon.app.reon.api.ApiService

import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignupActivity : AppCompatActivity() {

    private var progressDialog: ProgressDialogManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        progressDialog = ProgressDialogManager(this)

        btnSignup.setOnClickListener {
            doSignup()
        }
    }

    private fun doSignup() {
        // Disini program ngecek dulu kalo edittextnya kosong apa ngga
        // Kalo ada salah satu yg kosong, dia bakal keluar Alert
        if (edtEmail.text.toString() == "" || edtPassword.text.toString() == "" ||
                edtName.text.toString() == "" || edtPhone.text.toString() == "") {
            showAlert("Kolom tidak boleh kosong!")
        } else {
            // kalo aman, dia masuk ke method signup utk manggil API Signupnya
            signup(edtEmail.text.toString(), edtPassword.text.toString(),
                    edtName.text.toString(), edtPhone.text.toString())
        }
    }

    private fun signup(email: String, password: String, name: String, phone: String) {
        progressDialog?.show()
        val retrofit = Retrofit.Builder()
                .baseUrl(getString(R.string.BASE_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(ApiService::class.java)
        val call = api.signup(email, password, name, phone)
        call.enqueue(object : Callback<Base> {
            override fun onResponse(call: Call<Base>?, response: Response<Base>?) {
                // Kalo Internet aman, Server gaada gangguan, dia masuk ke method ini
                // Disini si program ngecek status == true atau false
                if (response!!.body().status) {
                    // Ini yang terjadi jika status = true
                    showToast("Email berhasil didaftarkan!")
                    finish()
                } else {
                    // Ini yang terjadi jika status = false
                    showToast("Email telah terdaftar, silahkan gunakan email lain")
                }
                progressDialog?.dismiss()
            }

            override fun onFailure(call: Call<Base>?, t: Throwable?) {
                Log.e("ERROR", t.toString())
                showToast("Terjadi kesalahan server, mohon coba beberapa saat lagi")
                progressDialog?.dismiss()
            }

        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
        builder.setPositiveButton(android.R.string.ok, null)
        builder.show()
    }

}
