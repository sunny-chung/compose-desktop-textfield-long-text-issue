import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import kotlinx.coroutines.launch

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val longText = (0 .. 300 * 1000).map { // around 300K data
                val c = "abcdefghijklmnABCDEFGHIJK123456\":{}[] \n".random().toString()
                if (c == "\n") {
                    "$c    "
                } else {
                    c
                }
            }
                .joinToString("")

            text = longText
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = text,
            onValueChange = { _ ->},
            textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace),
            readOnly = true,
            modifier = Modifier.align(Alignment.Center)
                .fillMaxSize()
                .verticalScroll(scrollState),
        )
        VerticalScrollbar(
            rememberScrollbarAdapter(scrollState),
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}

fun main() = application {
    Window(
        state = WindowState(width = 400.dp, height = 500.dp),
        title = "Compose Desktop TextField long text issue",
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}
