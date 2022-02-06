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
import androidx.navigation.NavController
import com.google.accompanist.insets.systemBarsPadding
import com.homework.project.R
import com.homework.project.data.UserPreferences

@Composable
fun UserProfile(
    onBackPress: () -> Unit
) {
    val firstName = UserPreferences.read("FirstName", "John")
    val lastName = UserPreferences.read("LastName", "Doe")

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
                Text(
                    text = "First name: $firstName \nLast name: $lastName",
                    style = MaterialTheme.typography.h4
                )
            }
        }
    }
}