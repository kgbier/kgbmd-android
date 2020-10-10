package com.kgbier.kgbmd.view.component

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.util.LiveDataDisposeBag
import com.kgbier.kgbmd.util.bind
import com.kgbier.kgbmd.util.disposeBy
import com.kgbier.kgbmd.util.viewModels
import com.kgbier.kgbmd.view.ui.mediaentitydetails.MediaEntityDetailsView
import com.kgbier.kgbmd.view.viewmodel.MediaEntityDetailsViewModel

@SuppressLint("ViewConstructor")
class MediaEntityDetailsList(context: MainActivity, titleId: String) : MediaEntityDetailsView(context) {

    private val disposeBag = LiveDataDisposeBag()

    private val mediaEntityDetailsViewModel: MediaEntityDetailsViewModel by context.viewModels(titleId) {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                MediaEntityDetailsViewModel(titleId) as T
        }
    }

    init {
        mediaEntityDetailsViewModel.state.bind(context) {
            if (it is MediaEntityDetailsViewModel.DetailsState.Loaded) {
                detailsAdapter.submitList(it.details)
            }
        }.disposeBy(disposeBag)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposeBag.dispose()
    }
}
