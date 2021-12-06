package com.example.myapplication.repository.db

import android.provider.BaseColumns
import com.example.myapplication.domain.Size

object ApparelContract {
    const val DB_NAME = "ApparelDB"
    const val DB_VERSION = 1

    object ApparelEntry : BaseColumns {
        const val DB_TABLE = "Apparel"
        const val COLUMN_ID = "Id"
        const val COLUMN_PICTURE = "Picture"
        const val COLUMN_NAME = "Name"
        const val COLUMN_COMPANY = "Company"
        const val COLUMN_SIZE = "Size"
        const val COLUMN_DESCRIPTION = "Description"
        const val COLUMN_COMPOSITION = "Composition"
    }
}