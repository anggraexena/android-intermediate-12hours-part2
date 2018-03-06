package com.example.gungde.intermediate12hours_2

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.example.gungde.intermediate12hours_2.model.User
import com.example.gungde.intermediate12hours_2.tools.ProgressDialogManager
import com.reon.app.reon.api.ApiService

import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private var progressDialog: ProgressDialogManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progressDialog = ProgressDialogManager(this)

        if(getSharedPreferences("MYAPP", Context.MODE_PRIVATE).getBoolean("login",false)){
            val i = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        }

        btnLogin.setOnClickListener {
            doLogin()
        }

        btnSignup.setPaintFlags(btnSignup.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        btnSignup.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
        }
    }

    private fun doLogin() {
        // Disini program ngecek dulu kalo edittextnya kosong apa ngga
        // Kalo ada salah satu yg kosong, dia bakal keluar Alert
        if (edtEmail.text.toString() == "" || edtPassword.text.toString() == "") {
            showAlert("Kolom tidak boleh kosong!")
        } else {
            // kalo aman, dia masuk ke method login
            login(edtEmail.text.toString(), edtPassword.text.toString())
        }
    }

    private fun login(email: String, password: String) {
        progressDialog?.show()
        val retrofit = Retrofit.Builder()
                .baseUrl(getString(R.string.BASE_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(ApiService::class.java)
        val call = api.login(email, password)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                // Kalo Internet aman, Server gaada gangguan, dia masuk ke method ini
                // Disini si program ngecek status == true atau false
                if (response!!.body().status) {
                    // Ini yang terjadi jika status = true
                    // Disini program juga akan menyimpan beberapa data jika login berhasil
                    // Disini dia menyimpan nilai boolean berupa true ke dalam Session
                    // Dia juga menyimpan nama dan userId akun User yang login
                    // Dia simpen semua itu ke dalam cache dengan tujuan jika user keluar dari
                    // aplikasi, si user ga perlu login lagi karena sessionnya sudah disimpan berupa
                    // true
                    val pref = getSharedPreferences("MYAPP", Context.MODE_PRIVATE)
                    val editor = pref.edit()
                    editor.putBoolean("login", true) // ini yang buat user ga perlu login lagi
                    editor.putString("name", response.body().data.name) // ini namanya disimpen
                    editor.putString("userId", response.body().data._id) // ini idnya juga disimpen
                    editor.apply() // nah ini metode ini yang bakal nyimpen datanya ke dalam session
                    val i = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(i)
                    finish()
                } else {
                    showToast("Password atau email tidak cocok!")
                }
                progressDialog?.dismiss()
            }

            override fun onFailure(call: Call<User>?, t: Throwable?) {
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
