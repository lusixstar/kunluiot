package com.example.kiotsdk.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import com.kunluiot.sdk.bean.family.FolderBean
import com.kunluiot.sdk.bean.scene.SceneOneKeyBean

class DiffRoomListCallback : DiffUtil.ItemCallback<FolderBean>() {

    override fun areItemsTheSame(oldItem: FolderBean, newItem: FolderBean): Boolean {
        return oldItem.folderId == newItem.folderId
    }

    override fun areContentsTheSame(oldItem: FolderBean, newItem: FolderBean): Boolean {
        return oldItem.folderName == newItem.folderName
    }
}

class DiffOneKeyListCallback : DiffUtil.ItemCallback<SceneOneKeyBean>() {

    override fun areItemsTheSame(oldItem: SceneOneKeyBean, newItem: SceneOneKeyBean): Boolean {
        return oldItem.sceneId == newItem.sceneId
    }

    override fun areContentsTheSame(oldItem: SceneOneKeyBean, newItem: SceneOneKeyBean): Boolean {
        return oldItem.sceneId == newItem.sceneId
    }
}