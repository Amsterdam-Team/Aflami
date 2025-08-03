package com.amsterdam.repository.utils

suspend fun <Local, Remote, Entity> getCachedOrRemoteData(
    deleteExpired: suspend () -> Unit,
    getFromLocal: suspend () -> List<Local>,
    mapFromLocalToEntity: (Local) -> Entity,
    getFromRemote: suspend () -> List<Remote>,
    saveRemoteToDatabase: suspend (List<Remote>) -> Unit,
    mapFromRemoteToEntity: (Remote) -> Entity,
): List<Entity> {
    deleteExpired()
    return getFromLocal()
        .takeIf(List<Local>::isNotEmpty)
        ?.map(mapFromLocalToEntity)
        ?: getFromRemote()
            .let {
                saveRemoteToDatabase(it)
                it.map(mapFromRemoteToEntity)
            }
}