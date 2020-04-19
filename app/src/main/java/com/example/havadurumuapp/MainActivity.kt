package com.example.havadurumuapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var spinnerAdapter  = ArrayAdapter.createFromResource(this,R.array.sehirler,R.layout.spinnerteksatir)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spnSehirler.setPositiveButton("SEÇ")
        spnSehirler.adapter= spinnerAdapter

        spnSehirler.setOnItemSelectedListener(this)

        verileriGetir("İstanbul")

        refresh.setOnClickListener {


            verileriGetir("İstanbul")

        }

    }

    fun verileriGetir(sehir: String){

        val url ="https://api.openweathermap.org/data/2.5/weather?q="+sehir+",tr&appid=755c2ab8eea2f39c7b539770f7b4b6a0&lang=tr"
        val havadurumuObjectRequest = JsonObjectRequest(Request.Method.GET,url,null,object : Response.Listener<JSONObject>
        {
            override fun onResponse(response: JSONObject?) {
                var main = response?.getJSONObject("main")
                var sicaklik= main?.getString("temp")

                var celciusSicaklik =(sicaklik?.toFloat())?.minus(273)
                var intCelcius = celciusSicaklik?.toInt()
                tvSicaklik.text= intCelcius.toString()

                var sehirAdi = response?.getString("name")
                tvSehir.text= sehirAdi
                var weather = response?.getJSONArray("weather")
                var aciklama = weather?.getJSONObject(0)?.getString("description")
                tvAciklama.text=aciklama

                var icon = weather?.getJSONObject(0)?.getString("icon")
                var resimDosya = resources.getIdentifier("icon"+icon?.sonKarakteriSil(),"drawable",packageName)
                imgHavaDurumu.setImageResource(resimDosya)

                tvTarih.text= tarihYazdir()

                var nem= main?.getString("humidity")
                tvNem.text= "Nem= %"+nem


                var tempmin= main?.getString("temp_min")
                var minSicaklik =(tempmin?.toFloat())?.minus(273)
                var minCelcius = minSicaklik?.toInt()
                tvEnDusuk.text= "En Düşük="+minCelcius+"°C"

                var tempmax= main?.getString("temp_max")
                var maxSicaklik =(tempmax?.toFloat())?.minus(273)
                var maxCelcius = maxSicaklik?.toInt()
                tvenyuksek.text= "En Yüksek="+maxCelcius+"°C"

                var wind = response?.getJSONObject("wind")
                var speed= wind?.getString("speed")
                tvRuzgar.text = "R. Hızı= "+speed+" m/s"


               var degres= wind?.getString("deg")

                var intDegres = degres?.toDouble()

                if(intDegres!! >208 || intDegres<11){
                        tvYon.text="R.Yönü= Kuzey"
                    }else if(intDegres > 11 && intDegres<33){
                        tvYon.text="R.Yönü= Kuzey-Kuzey Batı"
                    }
                    else if(intDegres > 33 && intDegres<56){
                        tvYon.text="R.Yönü=Kuzey Batı"
                    }
                    else if(intDegres > 56 && intDegres<78){
                        tvYon.text="R.Yönü= Batı-Kuzey Batı"
                    }
                    else if(intDegres > 78 && intDegres<101){
                        tvYon.text="R.Yönü= Batı"
                    }
                    else if(intDegres > 101 && intDegres<123){
                        tvYon.text="R.Yönü= Batı-Güney Batı"
                    }
                    else if(intDegres > 123 && intDegres<146){
                        tvYon.text="R.Yönü= Güney Batı"
                    }
                    else if(intDegres > 146 && intDegres<168){
                        tvYon.text="R.Yönü= Güney-Güney Batı"
                    }
                    else if(intDegres > 168 && intDegres<191){
                        tvYon.text="R.Yönü= Güney"
                    }
                    else if(intDegres > 191 && intDegres<213){
                        tvYon.text="R.Yönü= Kuzey"
                    }
                    else if(intDegres > 213 && intDegres<236){
                        tvYon.text="R.Yönü= Güney-Güney Doğu"
                    }
                    else if(intDegres > 236 && intDegres<258){
                        tvYon.text="R.Yönü= Doğu-Güney Doğu"
                    }
                    else if(intDegres > 258 && intDegres<281){
                        tvYon.text="R.Yönü= Doğu"
                    }
                    else if(intDegres > 281 && intDegres<303){
                        tvYon.text="R.Yönü= Doğu-Kuzey Doğu"
                    }
                    else if(intDegres > 303 && intDegres<326){
                        tvYon.text="R.Yönü= Kuzey Doğu"
                    }
                    else if(intDegres > 326 && intDegres<348){
                        tvYon.text="R.Yönü= Kuzey-Kuzey Doğu"
                    }

            }

        },object : Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {
            }

        })

        MySingleton.getInstance(this)?.addToRequestQueue(havadurumuObjectRequest)

    }


    fun tarihYazdir():String{

        var takvim = Calendar.getInstance().time
        var formatlayici = SimpleDateFormat("d MMM YYYY EEE hh:mm:ss", Locale("tr"))
        var tarih = formatlayici.format(takvim)

        return  tarih


    }

     override fun onNothingSelected(parent: AdapterView<*>?) {
     }

     override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

         var secilenSehir = parent?.getItemAtPosition(position).toString()
         verileriGetir(secilenSehir)
     }


 }

private fun String?.sonKarakteriSil(): String? {

    return this?.substring(0,this.length-1)

}


