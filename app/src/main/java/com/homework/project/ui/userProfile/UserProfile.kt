package com.homework.project.ui.userProfile

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.systemBarsPadding
import com.homework.project.R
import com.homework.project.data.Ids
import com.homework.project.data.entity.User

@Composable
fun UserProfile(
    onBackPress: () -> Unit,
    userId: Ids = Ids,
    viewModel: UserProfileViewModel = viewModel()
) {
    val user: User? = viewModel.getUser() // TODO: Fix user being always null (problem likely in the viewModel)
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            TopAppBar {
                IconButton(
                    onClick = onBackPress
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.ArrowBack)
                    )
                }
                Text(
                    text = "Profile"
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(16.dp)
            ) {
                if (user != null) {
                    Text(
                        text = "First name: ${user.firstname} \nLast name: ${user.lastname}",
                        style = MaterialTheme.typography.h4
                    )
                }
            }
        }
    }
}