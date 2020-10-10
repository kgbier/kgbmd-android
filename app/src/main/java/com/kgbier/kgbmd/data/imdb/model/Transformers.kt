package com.kgbier.kgbmd.data.imdb.model

import com.kgbier.kgbmd.domain.imdb.operation.ImageResizer
import com.kgbier.kgbmd.domain.model.Image

fun transformImageUrl(imageUrl: String): Image = imageUrl.let {
    Image(
        ImageResizer.resize(it, ImageResizer.SIZE_WIDTH_HINT),
        ImageResizer.resize(it, ImageResizer.SIZE_WIDTH_THUMBNAIL),
        ImageResizer.resize(it, ImageResizer.SIZE_WIDTH_LARGE),
    )
}
