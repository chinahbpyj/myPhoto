package com.pyj.myphoto.view.ui.setting

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pyj.myphoto.MainActivity
import com.pyj.myphoto.R
import com.pyj.myphoto.impl.PickPhotoInterface
import com.pyj.myphoto.view.ui.SwitchView

import com.pyj.myphoto.view.ui.TopAppBar
import com.pyj.myphoto.viewmodel.app.AppUiState
import com.pyj.myphoto.viewmodel.app.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Setting(
    appViewModel: AppViewModel,
    enabledSwitch: Boolean,
    appUiState: AppUiState,
    onBack: () -> Unit,
    pickPhoto: () -> Unit,
    daily: () -> Unit,
    imageDetail: (String) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = stringResource(id = R.string.setting),
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0x00000000),
            )
        )

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Row(
                modifier = Modifier
                    .semantics { role = Role.Tab }
                    .height(80.dp)
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 15.dp)
                    .shadow(
                        elevation = 3.dp,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "暗黑模式",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        lineHeight = 24.0.sp,
                        letterSpacing = 0.sp,
                    ),
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .weight(1f),
                )

                SwitchView(
                    modifier = Modifier.padding(start = 15.dp, end = 10.dp),
                    checked = appUiState.userData?.darkTheme ?: false,
                    enabledSwitch = enabledSwitch,
                    onCheckedChange = { appViewModel.setUserDarkTheme(it) }
                )
            }

            Row(
                modifier = Modifier
                    .semantics { role = Role.Tab }
                    .height(80.dp)
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 15.dp)
                    .shadow(
                        elevation = 3.dp,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "1列/2列",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        lineHeight = 24.0.sp,
                        letterSpacing = 0.sp,
                    ),
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .weight(1f),
                )

                SwitchView(
                    modifier = Modifier.padding(start = 15.dp, end = 10.dp),
                    checked = appUiState.userData?.row ?: false,
                    enabledSwitch = true,
                    onCheckedChange = { appViewModel.setUserShowRow(it) }
                )
            }

            Row(
                modifier = Modifier
                    .semantics { role = Role.Tab }
                    .height(80.dp)
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 15.dp)
                    .shadow(
                        elevation = 3.dp,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                    )
                    .clickable {
                        pickPhoto()

                        val mainActivity = context as MainActivity
                        mainActivity.setPickPhotoInterface(object : PickPhotoInterface {
                            override fun onSuccess(uri: Uri) {
                                imageDetail(uri.toString())
                            }

                            override fun onFail() {

                            }
                        })
                    },
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "图库分享",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        lineHeight = 24.0.sp,
                        letterSpacing = 0.sp,
                    ),
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .weight(1f),
                )
            }

            Row(
                modifier = Modifier
                    .semantics { role = Role.Tab }
                    .height(80.dp)
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 15.dp)
                    .shadow(
                        elevation = 3.dp,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                    )
                    .clickable {
                        daily()
                    },
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.daily),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        lineHeight = 24.0.sp,
                        letterSpacing = 0.sp,
                    ),
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .weight(1f),
                )
            }
        }
    }
}