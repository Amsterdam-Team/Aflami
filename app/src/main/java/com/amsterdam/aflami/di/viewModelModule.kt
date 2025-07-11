package com.amsterdam.aflami.di

import com.example.viewmodel.search.GlobalSearchViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::GlobalSearchViewModel)


}
