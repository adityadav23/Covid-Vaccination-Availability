package com.example.covid.vaccination

import java.net.Inet4Address

data class CenterRVModal (
    val centerName: String,
    val centerAddress: String,
    val centerFromTime: String,
    val centerToTime: String,
    val feeType: String,
    val ageLimit: Int,
    val vaccineName: String,
    val availableCapacity: Int
        )