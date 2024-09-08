package dev.sudhanshu.taskmanager.view.start

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url


@Composable
fun Start(onSignUpClick: () -> Unit, onSignInClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        KamelImage(
            resource = asyncPainterResource(data = Url("https://www.pngall.com/wp-content/uploads/12/Illustration-PNG-Free-Image.png")),
            contentDescription = "Illustration",
            onLoading = { progress ->
                CircularProgressIndicator(
                    progress = { progress },
                )
            },
            modifier = Modifier
                .height(400.dp)
                .fillMaxWidth()
                .padding(40.dp, 50.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = "Welcome to TaskManager!",
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp, 0.dp),
            fontSize = 24.sp
        )
        Text(
            text = "Create, prioritize, and track your tasks with ease.",
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp, 20.dp),
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        SignUpButton(onSignUpClick)
        Spacer(modifier = Modifier.height(10.dp))
        SignInText(onSignInClick)
    }
}

@Composable
fun SignUpButton(onSignUpClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable(onClick = onSignUpClick),
        shape = RoundedCornerShape(50.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth().padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Sign Up",
                fontSize = 16.sp,
                color = Color.Black,
            )
        }
    }
}

@Composable
fun SignInText(onSignInClick: () -> Unit) {
    val annotatedText = buildAnnotatedString {
        append("Already registered? ")

        pushStringAnnotation(tag = "SIGN_IN", annotation = "SignIn")
        withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
            append("Sign In")
        }
        pop()
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = annotatedText,
            modifier = Modifier.clickable { onSignInClick() }
        )
    }
}

