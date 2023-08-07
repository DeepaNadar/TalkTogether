package com.example.talktogether.Model

import java.sql.Timestamp

data class MessageModel(
    var message:String?="",
    var senderId:String?="",
    var timestamp: Long?=0
)
