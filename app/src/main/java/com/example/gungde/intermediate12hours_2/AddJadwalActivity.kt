package com.example.gungde.intermediate12hours_2

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.gungde.intermediate12hours_2.model.Base
import com.example.gungde.intermediate12hours_2.tools.ProgressDialogManager
import com.reon.app.reon.api.ApiService

import kotlinx.android.synthetic.main.activity_add_jadwal.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddJadwalActivity : AppCompatActivity() {

    private var progressDialog: ProgressDialogManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_jadwal)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Atur Jadwal"
        progressDialog = ProgressDialogManager(this)

        if(intent.extras != null){
            //Ini dipanggil jika menggunakan fungsi Update
            setDetailToEdittext()
            btnAction.text = "Ubah"
        }

        btnAction.setOnClickListener {
            doAction()
        }
    }

    private fun setDetailToEdittext() {
        edtMatkul.setText(intent.getStringExtra("matkul"))
        edtKelas.setText(intent.getStringExtra("kelas"))
        edtRuang.setText(intent.getStringExtra("ruang"))
        edtHari.setText(intent.getStringExtra("hari"))
    }

    private fun doAction() {
        if (edtMatkul.text.toString() == "" || edtHari.text.toString() == "" ||
                edtKelas.text.toString() == "" || edtRuang.text.toString() == "") {
            showAlert("Kolom tidak boleh kosong!")
        } else {
            if(intent.extras != null){
                updateJadwal(edtMatkul.text.toString(), edtHari.text.toString(),
                        edtKelas.text.toString(), edtRuang.text.toString())
            }else{
                tambahJadwal(edtMatkul.text.toString(), edtHari.text.toString(),
                        edtKelas.text.toString(), edtRuang.text.toString())
            }
        }
    }

    private fun tambahJadwal(matkul: String, hari: String, kelas: String, ruang: String) {
        progressDialog?.show()
        val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(ApiService::class.java)
        val call = api.addJadwal(kelas, hari, matkul, ruang)
        call.enqueue(object : Callback<Base> {
            override fun onResponse(call: Call<Base>?, response: Response<Base>?) {
                if (response!!.body().status) {
                    showToast("Berhasil menambahkan data")
                    finish()
                } else {
                    showToast("Gagal menambahkan data")
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

    private fun updateJadwal(matkul: String, hari: String, kelas: String, ruang: String) {
        progressDialog?.show()
        val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(ApiService::class.java)
        val id = intent.getStringExtra("id")
        val call = api.updateJadwal(id,kelas, hari, matkul, ruang)
        call.enqueue(object : Callback<Base> {
            override fun onResponse(call: Call<Base>?, response: Response<Base>?) {
                if (response!!.body().status) {
                    showToast("Berhasil memperbaharui data")
                    finish()
                } else {
                    showToast("Gagal memperbaharui data")
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
