package com.example.androidpracticumcustomview.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
    transparencyAnimationTime: Int = 5000,
    movementAnimationTime: Int = 5000
) {
    val coroutineScope = rememberCoroutineScope()

    val firstChildOffsetY = remember { Animatable(0f) }
    val secondChildOffsetY = remember { Animatable(0f) }

    val transparency = remember { Animatable(0f) }

    // Блок активации анимации при первом запуске
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            firstChildOffsetY.animateTo(
                targetValue = -1f,
                animationSpec = tween(movementAnimationTime)
            )

        }
        coroutineScope.launch {
            secondChildOffsetY.animateTo(
                targetValue = 1f,
                animationSpec = tween(movementAnimationTime)
            )
        }
        coroutineScope.launch {
            transparency.animateTo(
                targetValue = 1.0f,
                animationSpec = tween(transparencyAnimationTime)
            )
        }
    }

    // Основной контейнер
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        firstChild?.let { child ->
            Box(
                modifier = Modifier
                    .align(BiasAlignment(0f, firstChildOffsetY.value))
                    .alpha(transparency.value)
            ) {
                child()
            }
        }
        secondChild?.let { child ->
            Box(
                modifier = Modifier
                    .align(BiasAlignment(0f, secondChildOffsetY.value))
                    .alpha(transparency.value)
            ) {
                child()
            }
        }
    }
}