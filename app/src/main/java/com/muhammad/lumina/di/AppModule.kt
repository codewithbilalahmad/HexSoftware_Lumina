package com.muhammad.lumina.di

import com.muhammad.lumina.LuminaApplication
import com.muhammad.lumina.data.EditHistoryManager
import com.muhammad.lumina.data.EditedPhotoExporter
import com.muhammad.lumina.data.ImageUtilsRepositoryImp
import com.muhammad.lumina.domain.repository.ImageUtilsRepository
import com.muhammad.lumina.domain.repository.PhotoExporter
import com.muhammad.lumina.presentation.screens.edit_photo.EditPhotoViewModel
import com.muhammad.lumina.presentation.screens.home.HomeViewModel
import com.muhammad.lumina.presentation.screens.view_photo.ViewPhotoViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { LuminaApplication.INSTANCE }
    single {
        get<LuminaApplication>().applicationScope
    }
    single { EditHistoryManager() }
    singleOf(::EditedPhotoExporter).bind<PhotoExporter>()
    singleOf(::ImageUtilsRepositoryImp).bind<ImageUtilsRepository>()
    viewModelOf(::HomeViewModel)
    viewModelOf(::EditPhotoViewModel)
    viewModelOf(::ViewPhotoViewModel)
}