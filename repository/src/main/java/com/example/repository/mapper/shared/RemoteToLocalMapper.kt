package com.example.repository.mapper.shared

interface RemoteToLocalMapper<Remote, Local> {
    fun toLocal(remote: Remote, args: List<Any> = emptyList()): Local
    fun toLocalList(remoteList: List<Remote>, args: List<Any> = emptyList()): List<Local> =
        remoteList.map { toLocal(it, args) }
}