package fr.upjv.firstproject.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.upjv.firstproject.ui.screen.MainScreen
import fr.upjv.firstproject.ui.screen.SecondScreen
import kotlinx.serialization.Serializable
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ApplicationNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = ApplicationNavigationPath.Home,
        // quand tu vas vers un nouvel écran
        enterTransition = {
            fadeIn(animationSpec = tween(120)) +
                    slideInHorizontally(
                        animationSpec = tween(400, easing = FastOutSlowInEasing)
                    ) { fullWidth -> fullWidth } +          // arrive depuis la droite
                    scaleIn(
                        initialScale = 0.8f,
                        animationSpec = tween(400, easing = EaseOutBack)
                    )
        },
        // quand tu quittes l'écran courant
        exitTransition = {
            fadeOut(animationSpec = tween(120)) +
                    slideOutHorizontally(
                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                    ) { fullWidth -> -fullWidth / 3 } +      // glisse un peu vers la gauche
                    scaleOut(
                        targetScale = 1.05f,
                        animationSpec = tween(300, easing = LinearOutSlowInEasing)
                    )
        },
        // retour arrière → inverse visuel
        popEnterTransition = {
            fadeIn(animationSpec = tween(120)) +
                    slideInHorizontally(
                        animationSpec = tween(400, easing = FastOutSlowInEasing)
                    ) { fullWidth -> -fullWidth } +          // arrive depuis la gauche
                    scaleIn(
                        initialScale = 0.8f,
                        animationSpec = tween(400, easing = EaseOutBack)
                    )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(120)) +
                    slideOutHorizontally(
                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                    ) { fullWidth -> fullWidth / 3 } +       // glisse un peu vers la droite
                    scaleOut(
                        targetScale = 1.05f,
                        animationSpec = tween(300, easing = LinearOutSlowInEasing)
                    )
        }
    ) {
        composable<ApplicationNavigationPath.Home> {
            MainScreen(
                onButtonClick = { navController.navigate(route = ApplicationNavigationPath.Second) }
            )
        }

        composable<ApplicationNavigationPath.Second> {
            // 💥 wrapper “explosion” autour de la seconde screen
            ExplosiveScreenWrapper {
                SecondScreen(navigateBack = { navController.popBackStack() })
            }
        }
    }
}

object ApplicationNavigationPath {
    @Serializable
    data object Home

    @Serializable
    data object Second
}

/**
 * Wrapper qui fait :
 * - apparition du contenu en zoom + fade
 * - overlay d’explosion de particules
 */
@Composable
private fun ExplosiveScreenWrapper(
    content: @Composable () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        var visible by remember { mutableStateOf(false) }

        // lance l’animation d’apparition dès qu’on arrive sur l’écran
        LaunchedEffect(Unit) {
            visible = true
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = visible,
            enter = fadeIn(animationSpec = tween(150)) +
                    scaleIn(
                        initialScale = 0.7f,
                        animationSpec = tween(400, easing = EaseOutBack)
                    ),
            exit = fadeOut(animationSpec = tween(200)) +
                    scaleOut(
                        targetScale = 0.9f,
                        animationSpec = tween(300, easing = LinearOutSlowInEasing)
                    )
        ) {
            content()
        }

        // explosion geek par-dessus
        ExplosionOverlay()
    }
}

@Composable
private fun ExplosionOverlay() {
    // on démarre à 0, puis on lance l’animation vers 1
    var started by remember { mutableStateOf(false) }
    var finished by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        started = true
    }

    val progress by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        finishedListener = { finished = true },
        label = "explosionProgress"
    )

    // quand c’est fini de vrai, on arrête de dessiner
    if (finished && progress >= 1f) return

    // palette récupérée dans le contexte @Composable (autorisé)
    val palette = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // particules
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = this.center
            val baseRadius = size.minDimension * 0.02f
            val explosionRadius = size.minDimension * 0.5f * progress

            val particleCount = 16
            repeat(particleCount) { index ->
                val angle = (index / particleCount.toFloat()) * 2f * Math.PI.toFloat()
                val x = center.x + cos(angle) * explosionRadius
                val y = center.y + sin(angle) * explosionRadius

                val color = palette[index % palette.size]

                drawCircle(
                    color = color,
                    radius = baseRadius * (1f + progress * 1.5f),
                    center = Offset(x, y),
                    alpha = 1f - progress // disparaît progressivement
                )
            }
        }

        // texte "BOOM" au centre
        Text(
            text = "⚡ BOOM! ⚡",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 1f - progress),
        )
    }
}
