package com.example.covid.vaccination

import android.app.DatePickerDialog
import android.app.DownloadManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    
    private lateinit var searchButton: Button
    lateinit var pinCodeEditText: EditText
    lateinit var centerRecyclerView: RecyclerView
    lateinit var loadingProgressBar: ProgressBar
    lateinit var centerList: List<CenterRVModal>
    lateinit var centerRVAdapter: CenterRVAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        searchButton = findViewById(R.id.button_search)
        pinCodeEditText = findViewById(R.id.editText_pinCode)
        centerRecyclerView = findViewById(R.id.recyclerView_Centers)
        loadingProgressBar = findViewById(R.id.progressBar)
        centerList = ArrayList<CenterRVModal>()
        searchButton.setOnClickListener {
            
            val pinCode = pinCodeEditText.text.toString()
            //Check if pinCode is valid
            if(pinCode.length != 6){
                Toast.makeText(this, "Please enter valid PinCode..", Toast.LENGTH_SHORT)
                    .show()
            }else{
                (centerList as ArrayList<CenterRVModal>).clear()
                
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                
                val datePickerDialog = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view,year , month, dayOfMonth ->
                    loadingProgressBar.visibility = View.VISIBLE
                    val dateString:String = """$dayOfMonth-${month+1}-$year"""
                    getAppointmentDetails(pinCode , dateString)
                },
                    year,
                    month,
                    day
                )
                datePickerDialog.show()
            }
            
        }
    }

    private fun getAppointmentDetails(pinCode: String, date: String){
        val url =
            "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=" + pinCode + "&date=" + date
        val queue = Volley.newRequestQueue(this@MainActivity)
        //on below line we are creating a request variable for making our json object request.
        val request =
            //as we are getting json object response and we are making a get request.
            JsonObjectRequest(
                Request.Method.GET, url, null, {
                        response ->
                //this method is called when we get succesfull response from API.
                Log.e("TAG", "SUCCESS RESPONSE IS $response")
                //we are setting the visibility of progress bar as gone.
                    loadingProgressBar.visibility = View.GONE
                //on below line we are adding a try catch block.
                try {
                    //in try block we are creating a variable for center array and getting our array from our object.
                    val centerArray = response.getJSONArray("centers")
                    //on below line we are checking if the length of the array is 0.
                    //the zero length indicates that there is no data for the given pincode.
                    if (centerArray.length() == 0) {
                        Toast.makeText(this, "No Center Found", Toast.LENGTH_SHORT).show()
                    }
                    for (i in 0 until centerArray.length()) {
                        //on below line we are creating a variable for our center object.
                        val centerObj = centerArray.getJSONObject(i)
                        //on below line we are getting data from our session object and we are storing that in different variable.
                        val centerName: String = centerObj.getString("name")
                        val centerAddress: String = centerObj.getString("address")
                        val centerFromTime: String = centerObj.getString("from")
                        val centerToTime: String = centerObj.getString("to")
                        val fee_type: String = centerObj.getString("fee_type")
                        //on below line we are creating a variable for our session object
                        val sessionObj = centerObj.getJSONArray("sessions").getJSONObject(0)
                        val ageLimit: Int = sessionObj.getInt("min_age_limit")
                        val vaccineName: String = sessionObj.getString("vaccine")
                        val avaliableCapacity: Int = sessionObj.getInt("available_capacity")

                        //after extracting all the data we are passing this data to our modal class we have created a variable for it as center.
                        val center = CenterRVModal(
                            centerName,
                            centerAddress,
                            centerFromTime,
                            centerToTime,
                            fee_type,
                            ageLimit,
                            vaccineName,
                            avaliableCapacity
                        )
                        //after that we are passing this modal to our list on below line.
                        centerList = centerList + center
                    }

                    //on below line we are passing this list to our adapter class.
                    centerRVAdapter = CenterRVAdapter(centerList)
                    //on below line we are setting layout manager to our recycler view.
                    centerRecyclerView.layoutManager = LinearLayoutManager(this)
                    //on below line we are setting adapter to our recycler view.
                    centerRecyclerView.adapter = centerRVAdapter
                    //on below line we are notifying our adapter as the data is updated.
                    centerRVAdapter.notifyDataSetChanged()

                } catch (e: JSONException) {
                    //below line is for handling json exception.
                    e.printStackTrace();
                }
            },
                { error ->
                    //this method is called when we get any error while fetching data from our API
                    Log.e("TAG", "RESPONSE IS $error")
                    //in this case we are simply dislaying a toast message.
                    Toast.makeText(this@MainActivity, "Failed to get response", Toast.LENGTH_SHORT)
                        .show()
                })
        //at last we are adding our request to our queue.
        queue.add(request)
    }

}