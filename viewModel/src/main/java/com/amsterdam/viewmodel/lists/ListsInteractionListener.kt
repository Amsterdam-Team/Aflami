package com.amsterdam.viewmodel.lists

interface ListsInteractionListener {
    fun onClickAddCustomList()

    fun onListClick(
        listId: Long,
        listName: String,
    )

    fun onClickRetryFetchList()
}