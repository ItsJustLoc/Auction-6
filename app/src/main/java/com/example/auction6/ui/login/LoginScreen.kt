package com.example.auction6.ui.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.auction6.ui.components.AuctionTextField
import com.example.auction6.ui.theme.RetroBorder
import com.example.auction6.ui.theme.RetroBlue
import com.example.auction6.ui.theme.RetroInk
import com.example.auction6.ui.theme.RetroMuted
import com.example.auction6.ui.theme.RetroOrange
import com.example.auction6.ui.theme.RetroCream
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontStyle
@Composable
fun LoginScreen(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onGoToRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "AUCTION6",
            style = MaterialTheme.typography.displayLarge,
            color = RetroInk,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Where the garage meets the marketplace",
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 17.sp,
                fontStyle = FontStyle.Italic,
                letterSpacing = 0.5.sp
            ),
            color = RetroMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )


        Spacer(modifier = Modifier.height(24.dp))

        AuctionTextField(
            value = state.email,
            onValueChange = onEmailChange,
            label = "Email"
        )

        Spacer(modifier = Modifier.height(8.dp))

        AuctionTextField(
            value = state.password,
            onValueChange = onPasswordChange,
            label = "Password",
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onLoginClick,
            enabled = state.isLoginEnabled,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = RetroOrange,
                contentColor = RetroCream
            )
        ) {
            Text("LOGIN", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onGoToRegisterClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, RetroBlue),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = RetroBlue)
        ) {
            Text("CREATE ACCOUNT")
        }

        state.errorMessage?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(it, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
        }
    }
}
