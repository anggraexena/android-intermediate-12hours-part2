package com.example.gungde.intermediate12hours_2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.gungde.intermediate12hours_2.adapter.ListAdapter
import com.example.gungde.intermediate12hours_2.model.Jadwal
import com.example.gungde.intermediate12hours_2.tools.ProgressDialogManager
import com.reon.app.reon.api.ApiService

import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private var mAdapter: ListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val pref = getSharedPreferences("MYAPP", Context.MODE_PRIVATE)
        txWelcome.text = "Halo ${pref.getString("name",null)}"

        swipeRefresh.setOnRefreshListener(this)
        setRecyclerView()

        btnAdd.setOnClickListener {
            val i = Intent(this@MainActivity, AddJadwalActivity::class.java)
            startActivity(i)
        }
    }

    private fun setRecyclerView() {
        swipeRefresh.isRefreshing = true
        rvList.layoutManager = LinearLayoutManager(this)
        val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(ApiService::class.java)
        val call = api.getJadwals()
        call.enqueue(object : Callback<ArrayList<Jadwal>> {
            override fun onResponse(call: Call<ArrayList<Jadwal>>?, response: Response<ArrayList<Jadwal>>?) {
                if(response!!.body().isNotEmpty()){
                    mAdapter = ListAdapter(response.body(),this@MainActivity, ProgressDialogManager(this@MainActivity))
                    rvList.adapter = mAdapter
                    txEmpty.visibility = View.GONE
                }else{
                    txEmpty.visibility = View.VISIBLE
                }
                swipeRefresh.isRefreshing = false
            }

            override fun onFailure(call: Call<ArrayList<Jadwal>>?, t: Throwable?) {
                Log.e("ERROR", t.toString())
                swipeRefresh.isRefreshing = false
            }

        })
    }

    override fun onRefresh() {
        setRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        when (item.itemId) {
            R.id.logout -> {
                val pref = getSharedPreferences("MYAPP", Context.MODE_PRIVATE)
                val editor = pref.edit()
                editor.clear()
                editor.apply()
                val i = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(i)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
