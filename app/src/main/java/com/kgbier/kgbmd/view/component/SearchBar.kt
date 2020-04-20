package com.kgbier.kgbmd.view.component

import android.annotation.SuppressLint
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import com.kgbier.kgbmd.MainActivity
import com.kgbier.kgbmd.util.LiveDataDisposeBag
import com.kgbier.kgbmd.util.bind
import com.kgbier.kgbmd.util.disposeBy
import com.kgbier.kgbmd.view.ui.SearchBarView
import com.kgbier.kgbmd.view.viewmodel.MovieListSearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("ViewConstructor")
class SearchBar(context: MainActivity) : SearchBarView(context) {

    private val disposeBag = LiveDataDisposeBag()

    private val movieListSearchViewModel: MovieListSearchViewModel by context.viewModels()

    init {
        editTextSearch.hint = movieListSearchViewModel.hint

        movieListSearchViewModel.searchString.bind(context) {
            context.launch {
                withContext(Dispatchers.Default) {
                    if (editTextSearch.text.toString() != it) {
                        withContext(Dispatchers.Main) {
                            editTextSearch.setTextKeepState(it)
                        }
                    }
                }
            }
        }.disposeBy(disposeBag)

        editTextSearch.doOnTextChanged { text, _, _, _ ->
            movieListSearchViewModel.onSearchQueryUpdated(text.toString())
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposeBag.dispose()
    }
}