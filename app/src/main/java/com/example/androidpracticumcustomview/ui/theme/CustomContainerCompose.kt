package com.example.androidpracticumcustomview.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import kotlinx.coroutines.launch

/*
Задание:
Реализуйте необходимые компоненты;
Создайте проверку что дочерних элементов не более 2-х;
Предусмотрите обработку ошибок рендера дочерних элементов.
Задание по желанию:
Предусмотрите параметризацию длительности анимации.
 */
@Composable
fun CustomContainerCompose(
    firstChild: @Composable (() -> Unit)?,
    secondChild: @Composable (() -> Unit)?,
    transparencyAnimationTime: Int = 2000,
    movementAnimationTime: Int = 5000
) {
    var containerHeight by remember { mutableFloatStateOf(0f) }

    var firstChildHeight by remember { mutableFloatStateOf(0f) }
    var secondChildHeight by remember { mutableFloatStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()

    val firstChildOffsetY = remember { Animatable(0f) }
    val secondChildOffsetY = remember { Animatable(0f) }

    val transparency = remember { Animatable(0f) }

    // Блок активации анимации при первом запуске
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            firstChildOffsetY.animateTo(
                targetValue = (firstChildHeight - containerHeight) / 2,
                animationSpec = tween(movementAnimationTime)
            )

        }
        coroutineScope.launch {
            secondChildOffsetY.animateTo(
                targetValue = (containerHeight - secondChildHeight) / 2,
                animationSpec = tween(movementAnimationTime)
            )
        }
        coroutineScope.launch {
            transparency.animateTo(
                targetValue = 1f,
                animationSpec = tween(transparencyAnimationTime)
            )
        }
    }

    // Основной контейнер
    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { size ->
                containerHeight = size.height.toFloat()
            },
        contentAlignment = Alignment.Center
    ) {
        firstChild?.let { child ->
            Box(
                modifier = Modifier
                    .onSizeChanged { size ->
                        firstChildHeight = size.height.toFloat()
                    }
                    .graphicsLayer(
                        alpha = transparency.value,
                        translationY = firstChildOffsetY.value
                    ),
                content = { child() }
            )
        }
        secondChild?.let { child ->
            Box(
                modifier = Modifier
                    .onSizeChanged { size ->
                        secondChildHeight = size.height.toFloat()
                    }
                    .graphicsLayer(
                        alpha = transparency.value,
                        translationY = secondChildOffsetY.value
                    ),
                content = { child() }
            )
        }
    }
}