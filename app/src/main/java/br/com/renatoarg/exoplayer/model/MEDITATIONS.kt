package br.com.renatoarg.exoplayer.model

import android.graphics.Bitmap
import android.net.Uri

object MEDITATIONS {

    const val MEDITATION = "meditation"

    val meditations = arrayListOf(
        Meditation(
            "INXS",
            "Beautiful girl",
            "http://www.sylvaniaalabama.com/images/ms_250x250/otherimage/solidstockart-stock-photo-beautiful-girl-wearing-hair--442444.jpg",
            "https://s3.amazonaws.com/wakingup-dev/courses/images/000/000/014/thumb_x1/img-64d990503c4.jpg",
            "https://elmayorportaldegerencia.com/Musica/Audios/Inxs/Beautiful%20Girl.mp3",
            null
        ),
        Meditation(
            "Welcome",
            "",
            "https://s3.amazonaws.com/wakingup-dev/courses/images/000/000/461/original/img-748220f43ae.jpg",
            "https://s3.amazonaws.com/wakingup-dev/courses/images/000/000/461/thumb_x1/img-748220f43ae.jpg",
            "https://s3.amazonaws.com/wakingup-dev/courses/mp3s/000/000/461/original/img-0531800fc90.mp3",
            null
        ),
        Meditation(
            "The Logic of Practice",
            "Sam Harris discusses why it makes sense to train one's mind though meditation.",
            "https://s3.amazonaws.com/wakingup-dev/courses/images/000/000/021/original/img-e30c946f5e9.jpg",
            "https://s3.amazonaws.com/wakingup-dev/courses/images/000/000/021/thumb_x1/img-e30c946f5e9.jpg",
            "https://s3.amazonaws.com/wakingup-dev/courses/mp3s/000/000/021/original/img-348b3775d78.mp3",
            null
        ),
        Meditation(
            "The Cure for Boredom",
            "Sam Harris talks about meditation as an antidote to boredom.",
            "https://s3.amazonaws.com/wakingup-dev/courses/images/000/000/014/original/img-64d990503c4.jpg",
            "https://s3.amazonaws.com/wakingup-dev/courses/images/000/000/014/thumb_x1/img-64d990503c4.jpg",
            "https://s3.amazonaws.com/wakingup-dev/courses/mp3s/000/000/014/original/img-246643d7ccc.mp3",
            null
        )
    )

}
