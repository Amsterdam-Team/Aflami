package com.example.viewmodel.search.actorSearch

interface SearchByActorInteractionListener {
    fun onKeywordValueChanged(keyword: String)
    fun onNavigateBackClicked()
    fun onRetryQuestClicked()
}