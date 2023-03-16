package com.example.scrolllayout

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrolllayout.ui.theme.ScrollLayoutTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScrollLayoutTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Box {
                        val density = LocalDensity.current.density
                        val list = remember { getList() }
                        val titleBarHeight = remember { 50.dp }
                        val titleBarOffset = remember { mutableStateOf(0f) }
                        val nestedScrollConnection = remember {
                            object : NestedScrollConnection {
                                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                                    val delta = available.y
                                    titleBarOffset.value = (titleBarOffset.value + delta).coerceAtLeast(- titleBarHeight.value * density).coerceAtMost(0f)
                                    return Offset.Zero
                                }
                            }
                        }
                        ListView(
                            titleBarHeight = titleBarHeight,
                            modifier = Modifier,
                            list = list,
                            nestedScrollConnection = nestedScrollConnection
                        )
                        TitleBar(
                            height = titleBarHeight,
                            titleBarOffset = titleBarOffset.value
                        )
                    }
                }
            }
        }
    }

    fun getList(): List<Int> {
        val list: MutableList<Int> = mutableListOf()
        for (i in 0 until 50) {
            list.add(i)
        }
        return list
    }
}

@Composable
fun TitleBar(
    height: Dp,
    titleBarOffset: Float
) {

    Box(
        modifier = Modifier
            .height(height)
            .fillMaxWidth()
            .offset(y = (titleBarOffset / LocalDensity.current.density).dp)
            .background(Color.Blue)
    ) {
    }
}

@Composable
fun ListView(
    titleBarHeight: Dp,
    modifier: Modifier,
    list: List<Int>,
    nestedScrollConnection: NestedScrollConnection
) {

    val state = rememberLazyListState()

    LazyColumn(
        modifier = modifier
            .nestedScroll(nestedScrollConnection),
        state = state,
    ) {
        itemsIndexed(list) { index, item ->
            Wrapper() {
                Column() {
                    if (index == 0) Spacer(modifier = Modifier.size(titleBarHeight))
                    Text(
                        text = item.toString(),
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }
        }
    }
}

@Composable
fun Wrapper(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        content()
    }
}