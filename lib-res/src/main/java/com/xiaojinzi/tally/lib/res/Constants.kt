package com.xiaojinzi.tally.lib.res

import androidx.annotation.Keep

const val LOG_KEYWORDS_TALLY_DATASOURCE = "TallyDatasource"

const val APP_EVENT_REFRESH_BILL_CYCLE = "refreshBillCycle"

const val QQ_GROUP_LINK =
    "https://qm.qq.com/cgi-bin/qm/qr?authKey=x4cb%2Fi0rokSUVGc3jCEht9TIuOB2%2B6c%2BDx5nneGUsqHqAub6%2B0XyyJW4S7FQfRl4&k=pTaY0iXhbVqTDKJAycVKLBa7Lp-Vm7-A&noverify=0#"

@Keep
sealed class OssProcess(
    private val format: String? = null,
    open val thumbnailSize: Int? = null,
    private val interlace: Boolean? = true,
    private val blur: Boolean? = null,
) // 占位
{

    val styleStr = "x-oss-process=image${
        thumbnailSize?.run {
            "/resize,m_lfit,w_${this},h_${this}"
        } ?: ""
    }${
        format?.run {
            "/format,${this}"
        } ?: ""
    }${
        blur?.run {
            "/blur,r_10,s_10"
        } ?: ""
    }${
        interlace?.run {
            "/interlace,${if (this) 1 else 0}"
        } ?: ""
    }"

    data class DEFAULT(
        private val format: String? = null,
        override val thumbnailSize: Int? = 1000,
        private val interlace: Boolean? = true,
        private val blur: Boolean? = null,
    ) : OssProcess(
        format = format,
        thumbnailSize = thumbnailSize,
        interlace = interlace,
        blur = blur,
    )

    data class DEFAULT_BLUR(
        private val format: String? = "jpg",
        override val thumbnailSize: Int? = 1000,
        private val interlace: Boolean? = true,
    ) : OssProcess(
        format = format,
        thumbnailSize = thumbnailSize,
        interlace = interlace,
        blur = true,
    )

    data object THUMBNAIL100 : OssProcess(
        thumbnailSize = 100,
    )

    data object THUMBNAIL200 : OssProcess(
        thumbnailSize = 200,
    )

    data object THUMBNAIL400 : OssProcess(
        thumbnailSize = 400,
    )

    data object THUMBNAIL800 : OssProcess(
        thumbnailSize = 800,
    )

    data object THUMBNAIL1000 : OssProcess(
        thumbnailSize = 1000,
    )

    data object THUMBNAIL1600 : OssProcess(
        thumbnailSize = 1600,
    )

    data object THUMBNAIL2400 : OssProcess(
        thumbnailSize = 2400,
    )

}

@Keep
enum class SupportLoginMethod {
    // 微信登录
    WX,
    Google,
}