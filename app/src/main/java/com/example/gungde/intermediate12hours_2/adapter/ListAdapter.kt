package com.example.gungde.intermediate12hours_2.adapter

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.gungde.intermediate12hours_2.R
import com.example.gungde.intermediate12hours_2.model.Jadwal
import kotlinx.android.synthetic.main.item_row.view.*
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.gungde.intermediate12hours_2.AddJadwalActivity
import com.example.gungde.intermediate12hours_2.BuildConfig
import com.example.gungde.intermediate12hours_2.model.Base
import com.reon.app.reon.api.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by gungdeaditya on 2/22/18.
 */

class ListAdapter(private val jadwals: ArrayList<Jadwal>,private val activity : Activity, private val progressDialog :ProgressDialog) : RecyclerView.Adapter<ListAdapter.ListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return ListHolder(view)
    }

    override fun onBindViewHolder(holder: ListAdapter.ListHolder, position: Int) {
        val jadwal = jadwals[position]
        holder.view.txHari.text = jadwal.hari
        holder.view.txKelas.text = "Kelas : " + jadwal.kelas
        holder.view.txRuang.text = "Ruang : " + jadwal.ruang
        holder.view.txMatkul.text = jadwal.matkul

        // Method dibawah bikin listnya keluar Alert ketika dikliknya ditahan agak Lama
        holder.view.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(p0: View?): Boolean {
                showAlert(jadwal, position)
                return true
            }
        })
    }

    override fun getItemCount(): Int {
        return jadwals.size
    }

    inner class ListHolder(val view: View) : RecyclerView.ViewHolder(view)

    private fun hapusJadwal(id : String, position : Int) {
        progressDialog.show()
        val retrofit = Retrofit.Builder()
                .baseUrl(activity.getString(R.string.BASE_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(ApiService::class.java)
        val call = api.deleteJadwal(id)
        call.enqueue(object : Callback<Base> {
            override fun onResponse(call: Call<Base>?, response: Response<Base>?) {
                if (response!!.body().status) {
                    showToast("Berhasil menghapus")
                    jadwals.removeAt(position)
                    notifyItemRemoved(position)
                } else {
                    showToast("Gagal, Jadwal tidak dapat dihapus")
                }
                progressDialog.dismiss()
            }

            override fun onFailure(call: Call<Base>?, t: Throwable?) {
                Log.e("ERROR", t.toString())
                showToast("Terjadi kesalahan server, mohon coba beberapa saat lagi")
                progressDialog.dismiss()
            }

        })
    }

    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun showAlert(jadwal: Jadwal, position: Int) {
        val builder = AlertDialog.Builder(activity)
        val actions = arrayOf("Update", "Delete")
        builder.setTitle("Choose an action")
        builder.setItems(actions, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, pos: Int) {
                when(pos){
                //Update
                    0 -> {

                        // Kodingan dibawah yang bakal pindah ke Halaman Add dan juga membawa
                        // data yang mau diupdate

                        val i = Intent(activity, AddJadwalActivity::class.java)
                        i.putExtra("id", jadwal._id)
                        i.putExtra("hari", jadwal.hari)
                        i.putExtra("kelas", jadwal.kelas)
                        i.putExtra("matkul", jadwal.matkul)
                        i.putExtra("ruang", jadwal.ruang)
                        activity.startActivity(i)
                    }

                //Delete
                    1 -> {
                        hapusJadwal(jadwal._id,position)
                    }
                }
            }
        })

        val dialog = builder.create()
        dialog.show()
    }
}
