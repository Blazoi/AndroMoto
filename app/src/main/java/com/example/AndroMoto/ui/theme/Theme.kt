package com.example.AndroMoto.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Typography
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.load.engine.Resource
import com.example.AndroMoto.R

private val LightColors = lightColorScheme(
    primary = Green40,
    onPrimary = Color.White,
    primaryContainer = GreenContainerLight,
    onPrimaryContainer = OnGreenContainerLight,

    secondary = Gray40,
    onSecondary = Color.White,
    secondaryContainer = Gray90,
    onSecondaryContainer = Gray10,

    tertiary = Color(0xFF3F6B47),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD5EDD9),
    onTertiaryContainer = Color(0xFF0B3D12),

    background = Gray98,
    onBackground = Gray10,
    surface = Gray98,
    onSurface = Gray10,
    surfaceVariant = Color(0xFFE0E0E0),
    onSurfaceVariant = Gray30,
    surfaceDim = Color(0xFFDADADA),
    surfaceBright = Gray98,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Gray95,
    surfaceContainer = Color(0xFFEDEDED),
    surfaceContainerHigh = Color(0xFFE7E7E7),
    surfaceContainerHighest = Color(0xFFE1E1E1),

    outline = Gray50,
    outlineVariant = Gray80,

    error = Color(0xFFB3261E),
    onError = Color.White,
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),
)

private val DarkColors = darkColorScheme(
    primary = Green80,
    onPrimary = Color(0xFF003912),
    primaryContainer = GreenContainerDark,
    onPrimaryContainer = OnGreenContainerDark,

    secondary = Gray80,
    onSecondary = Gray20,
    secondaryContainer = Color(0xFF3F3F3F),
    onSecondaryContainer = Gray90,

    tertiary = Color(0xFFA8D5B0),
    onTertiary = Color(0xFF1B3A22),
    tertiaryContainer = Color(0xFF2E4F36),
    onTertiaryContainer = Color(0xFFC4F0CC),

    background = Color(0xFF121212),
    onBackground = Color(0xFFE3E3E3),
    surface = Color(0xFF121212),
    onSurface = Color(0xFFE3E3E3),
    surfaceVariant = Color(0xFF444444),
    onSurfaceVariant = Color(0xFFC4C4C4),
    surfaceDim = Color(0xFF121212),
    surfaceBright = Color(0xFF383838),
    surfaceContainerLowest = Color(0xFF0D0D0D),
    surfaceContainerLow = Color(0xFF1A1A1A),
    surfaceContainer = Color(0xFF1E1E1E),
    surfaceContainerHigh = Color(0xFF282828),
    surfaceContainerHighest = Color(0xFF333333),

    outline = Gray60,
    outlineVariant = Color(0xFF444444),

    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC),
)

val typography = Typography()

@Composable
fun AndroMotoTheme(
    darkTheme: Boolean = true,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}