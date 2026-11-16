package com.a.server.presentation.authentication.docs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a.server.core.util.toPrettyJson
import com.a.server.navigation.NavigationState
import com.a.server.presentation.authentication.docs.component.Request
import com.a.server.presentation.authentication.docs.component.Response
import org.koin.compose.koinInject

@Composable
fun AuthenticationDocsScreen(
    navigationState: NavigationState = koinInject(),
) {
    Content()
}

@Composable
private fun Content(

) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(start = 15.dp, end = 15.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        item("signInRequest") {
            Text(
                modifier = Modifier
                    .animateItem(),
                text = ""
            )
        }
        item("signInRequestData") {
            Card(
                modifier = Modifier
                    .animateItem(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = Request(
                            email = "string",
                            password = "string"
                        ).toPrettyJson()
                    )
                }
            }
        }
        item("signUpResponse") {
            Text(
                modifier = Modifier
                    .animateItem(),
                text = ""
            )
        }
        item("signUpResponseData") {
            Card(
                modifier = Modifier
                    .animateItem(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = Response(
                            success = "boolean",
                            message = "string"
                        ).toPrettyJson()
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    Content()
}