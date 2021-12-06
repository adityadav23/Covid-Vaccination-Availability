package com.example.covid.vaccination

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CenterRVAdapter(private val centerList: List<CenterRVModal>) :
    RecyclerView.Adapter<CenterRVAdapter.CenterRVViewHolder>() {

    class CenterRVViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val centerNameTV: TextView = itemView.findViewById(R.id.tv_centerName)
        val centerLocationTV: TextView = itemView.findViewById(R.id.tv_center_location)
        val centerTimingsTV: TextView = itemView.findViewById(R.id.tv_timings)
        val ageLimitTV: TextView = itemView.findViewById(R.id.tv_ageLimit)
        val vaccineNameTV: TextView = itemView.findViewById(R.id.tv_vaccineName)
        val vaccineFeesTV: TextView = itemView.findViewById(R.id.tv_vaccineFees)
        val availabilityTV: TextView = itemView.findViewById(R.id.tv_vaccineAvailability)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterRVViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.center_rv_item,parent,false)
        return CenterRVViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CenterRVViewHolder, position: Int) {
        val center = centerList[position]
        holder.centerNameTV.text = center.centerName
        holder.centerLocationTV.text = center.centerAddress
       holder.centerTimingsTV.text =
            ("From : " + center.centerFromTime + " To : " + center.centerToTime)
        holder.vaccineNameTV.text = center.vaccineName
        holder.ageLimitTV.text = "Age Limit : " + center.ageLimit.toString()
        holder.vaccineFeesTV.text = center.feeType
        holder.availabilityTV.text = "Availability : " + center.availableCapacity.toString()
    }

    override fun getItemCount(): Int {
    return centerList.size
    }
}