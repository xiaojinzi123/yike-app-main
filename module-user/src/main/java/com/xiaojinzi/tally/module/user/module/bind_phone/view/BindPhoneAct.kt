package com.xiaojinzi.tally.module.user.module.bind_phone.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.view.WindowCompat
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
import com.xiaojinzi.support.activity_stack.ActivityFlag
import com.xiaojinzi.support.annotation.ViewLayer
import com.xiaojinzi.support.compose.StateBar
import com.xiaojinzi.support.ktx.initOnceUseViewModel
import com.xiaojinzi.tally.lib.res.ui.APP_ACTIVITY_FLAG_LOGIN
import com.xiaojinzi.tally.module.base.support.AppRouterConfig
import com.xiaojinzi.tally.module.base.theme.AppTheme
import com.xiaojinzi.tally.module.base.view.BaseBusinessAct
import kotlinx.coroutines.InternalCoroutinesApi

@ActivityFlag(
    APP_ACTIVITY_FLAG_LOGIN,
)
@RouterAnno(
    hostAndPath = AppRouterConfig.USER_BIND_PHONE,
)
@ViewLayer
class BindPhoneAct : BaseBusinessAct<BindPhoneViewModel>() {

    @AttrValueAutowiredAnno("wxAuthId")
    var wxAuthId: String? = null

    override fun getViewModelClass(): Class<BindPhoneViewModel> {
        return BindPhoneViewModel::class.java
    }

    @OptIn(
        InternalCoroutinesApi::class,
        ExperimentalMaterial3Api::class,
        ExperimentalAnimationApi::class,
        ExperimentalFoundationApi::class,
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        initOnceUseViewModel {
            requiredViewModel().wxAuthIdInitData.value = wxAuthId
        }

        setContent {
            AppTheme {
                StateBar {
                    BindPhoneViewWrap()
                }
            }
        }

    }

}