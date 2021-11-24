package com.example.kiotsdk.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import com.kunluiot.sdk.bean.family.FolderBean

class DiffRoomListCallback : DiffUtil.ItemCallback<FolderBean>() {

    override fun areItemsTheSame(oldItem: FolderBean, newItem: FolderBean): Boolean {
        return oldItem.folderId == newItem.folderId
    }

    override fun areContentsTheSame(oldItem: FolderBean, newItem: FolderBean): Boolean {
        return oldItem.folderName == newItem.folderName
    }
}