package com.example.kiotsdk.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import com.kunluiot.sdk.bean.family.FolderBean
import com.kunluiot.sdk.bean.scene.*

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

class DiffSceneLinkedListCallback : DiffUtil.ItemCallback<SceneLinkBeanNew>() {

    override fun areItemsTheSame(oldItem: SceneLinkBeanNew, newItem: SceneLinkBeanNew): Boolean {
        return oldItem.ruleName == newItem.ruleName
    }

    override fun areContentsTheSame(oldItem: SceneLinkBeanNew, newItem: SceneLinkBeanNew): Boolean {
        return oldItem.ruleName == newItem.ruleName
    }
}

class DiffSceneIftttTasksListCallback : DiffUtil.ItemCallback<SceneIftttTasksListBeanNew>() {

    override fun areItemsTheSame(oldItem: SceneIftttTasksListBeanNew, newItem: SceneIftttTasksListBeanNew): Boolean {
        return oldItem.taskId == newItem.taskId
    }

    override fun areContentsTheSame(oldItem: SceneIftttTasksListBeanNew, newItem: SceneIftttTasksListBeanNew): Boolean {
        return oldItem.taskId == newItem.taskId
    }
}

class DiffSceneLinkedConditionListCallback : DiffUtil.ItemCallback<SceneConditionListBeanNew>() {

    override fun areItemsTheSame(oldItem: SceneConditionListBeanNew, newItem: SceneConditionListBeanNew): Boolean {
        return oldItem.devTid == newItem.devTid
    }

    override fun areContentsTheSame(oldItem: SceneConditionListBeanNew, newItem: SceneConditionListBeanNew): Boolean {
        return oldItem.devTid == newItem.devTid
    }
}