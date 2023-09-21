package com.example.fragment

import com.google.gson.annotations.SerializedName

data class ImageModel( @SerializedName("documents")
                       val documents: ArrayList<Documents>,

                       @SerializedName("meta")
                       val meta: Meta
) {
    /**
     * 이미지 검색 응답에서 단일 문서 혹은 결과를 나타내는 클래스.
     */
    data class Documents(
        @SerializedName("collection")
        val collection: String,

        @SerializedName("thumbnail_url")
        val thumbnailUrl: String,

        @SerializedName("image_url")
        val imageUrl: String,

        @SerializedName("width")
        val width: Int,

        @SerializedName("height")
        val height: Int,

        @SerializedName("display_sitename")
        val displaySitename: String,

        @SerializedName("doc_url")
        val docUrl: String,

        @SerializedName("datetime")
        val datetime: String
    )

    /**
     * 이미지 검색 응답에 대한 메타 정보를 나타내는 클래스.
     */
    data class Meta(
        @SerializedName("is_end")
        val isEnd: Boolean,

        @SerializedName("pageable_count")
        val pageableCount: Int,

        @SerializedName("total_count")
        val totalCount: Int
    )
}
