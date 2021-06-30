package com.timgortworst.cleanarchitecture.data.model

import androidx.room.Entity

@Entity(tableName = "remote_keys")
data class RemoteKey(val label: String, val nextKey: String?)