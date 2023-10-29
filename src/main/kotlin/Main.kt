import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.launch
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.HorizontalSplitPane
import org.jetbrains.compose.splitpane.rememberSplitPaneState

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
//            val longText = (0 .. 300 * 1000).map { // around 300K data
//                val c = "abcdefghijklmnABCDEFGHIJK123456\":{}[] \n".random().toString()
//                if (c == "\n") {
//                    "$c    "
//                } else {
//                    c
//                }
//            }
//                .joinToString("")

            var longText = Thread.currentThread().contextClassLoader.getResourceAsStream("long-resp.txt")!!.readAllBytes().decodeToString()
            longText = jacksonObjectMapper().readTree(longText).toPrettyString()
            println("longText has been prepared")
            text = text.copy(text = longText)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = text,
            onValueChange = { _ -> },
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

//        HorizontalSplitPane(splitPaneState = rememberSplitPaneState(initialPositionPercentage = 0.1f)) {
//            first(minSize = 5.dp) {
//                Text(text = "abc")
//            }
//            second(minSize = 300.dp) {
//                HorizontalSplitPane(splitPaneState = rememberSplitPaneState(initialPositionPercentage = 0.1f)) {
//                    first(minSize = 10.dp) {
//                        Text(text = "abc")
//                    }
//                    second(minSize = 200.dp) {
//                        Column (modifier = Modifier.padding(horizontal = 8.dp)) {
//                            Text(text = "def")
//                            Box(modifier = Modifier.fillMaxWidth().weight(1f).padding(vertical = 8.dp)) {
//                                TextField(
//                                    value = text,
//                                    onValueChange = { _ -> },
//                                    textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace),
//                                    readOnly = true,
//                                    modifier = Modifier.align(Alignment.Center)
//                                        .fillMaxSize()
//                                        .verticalScroll(scrollState),
//                                )
//                                VerticalScrollbar(
//                                    rememberScrollbarAdapter(scrollState),
//                                    modifier = Modifier.align(Alignment.CenterEnd)
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
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
