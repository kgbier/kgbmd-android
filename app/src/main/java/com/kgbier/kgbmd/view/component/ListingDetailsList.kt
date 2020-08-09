package com.kgbier.kgbmd.view.component

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.util.LiveDataDisposeBag
import com.kgbier.kgbmd.util.bind
import com.kgbier.kgbmd.util.disposeBy
import com.kgbier.kgbmd.util.viewModels
import com.kgbier.kgbmd.view.ui.listingdetails.ListingDetailsView
import com.kgbier.kgbmd.view.viewmodel.TitleDetailsViewModel

@SuppressLint("ViewConstructor")
class ListingDetailsList(context: MainActivity, titleId: String) : ListingDetailsView(context) {

    private val disposeBag = LiveDataDisposeBag()

    private val titleDetailsViewModel: TitleDetailsViewModel by context.viewModels(titleId) {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                TitleDetailsViewModel(titleId) as T
        }
    }

    init {
        titleDetailsViewModel.titleDetails.bind(context) {
            if (it is TitleDetailsViewModel.TitleDetailsState.Loaded) {
                listingAdapter.submitList(it.details)
            }
        }.disposeBy(disposeBag)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposeBag.dispose()
    }
}
