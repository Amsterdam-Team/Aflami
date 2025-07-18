package com.example.repository.mapper.shared

interface RemoteToLocalMapper<Remote, Local> {
    fun toLocal(remote: Remote): Local
    fun toLocalList(remoteList: List<Remote>): List<Local> = remoteList.map(::toLocal)
}