package com.example.livealbum

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
val images = listOf(
    LiveImage(
        1,
        true,
        R.raw.sample_video,
        "Prague main street",
        "Prague",
        LocalDate.parse("2018-04-12")


    ),
    LiveImage(
        2,
        false,
        R.drawable.img_sample,
        "Laptop on the desk",
        "Living room",
        LocalDate.parse("2019-06-15")

    ),
)