package com.taeyeon.xoduslol.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import xoduslol.composeapp.generated.resources.Galmuri11
import xoduslol.composeapp.generated.resources.Galmuri11_Bold
import xoduslol.composeapp.generated.resources.Galmuri11_Condensed
import xoduslol.composeapp.generated.resources.Res


val Galmuri11: FontFamily
    @Composable
    get () = FontFamily(
        Font(
            resource = Res.font.Galmuri11,
            weight = FontWeight.Normal,
            style = FontStyle.Normal,
        ),
        Font(
            resource = Res.font.Galmuri11_Bold,
            weight = FontWeight.Bold,
            style = FontStyle.Normal,
        ),
        Font(
            resource = Res.font.Galmuri11_Condensed,
            weight = FontWeight.Light,
            style = FontStyle.Normal,
        ),
    )

val AppTypography: Typography
    @Composable
    get() = Typography().let { baseline ->
        Typography(
            displayLarge = baseline.displayLarge.copy(fontFamily = Galmuri11),
            displayMedium = baseline.displayMedium.copy(fontFamily = Galmuri11),
            displaySmall = baseline.displaySmall.copy(fontFamily = Galmuri11),
            headlineLarge = baseline.headlineLarge.copy(fontFamily = Galmuri11),
            headlineMedium = baseline.headlineMedium.copy(fontFamily = Galmuri11),
            headlineSmall = baseline.headlineSmall.copy(fontFamily = Galmuri11),
            titleLarge = baseline.titleLarge.copy(fontFamily = Galmuri11),
            titleMedium = baseline.titleMedium.copy(fontFamily = Galmuri11),
            titleSmall = baseline.titleSmall.copy(fontFamily = Galmuri11),
            bodyLarge = baseline.bodyLarge.copy(fontFamily = Galmuri11),
            bodyMedium = baseline.bodyMedium.copy(fontFamily = Galmuri11),
            bodySmall = baseline.bodySmall.copy(fontFamily = Galmuri11),
            labelLarge = baseline.labelLarge.copy(fontFamily = Galmuri11),
            labelMedium = baseline.labelMedium.copy(fontFamily = Galmuri11),
            labelSmall = baseline.labelSmall.copy(fontFamily = Galmuri11),
        )
    }

/* materialkolor.com midContrast
val primary = Color(0xFF3A7D44)
val secondary = Color(0xFFDF6D14)
val tertiary = Color(0xFF638C6D)
val error = Color(0xFFD9401B)
val neutral = Color(0xFFE1EACD)
val neutralVariant = Color(0xFFBAD8B6) */

val primaryLight = Color(0xFF004A19)
val onPrimaryLight = Color(0xFF9EE5A1)
val primaryContainerLight = Color(0xFF3F8249)
val onPrimaryContainerLight = Color(0xFFFFFFFF)
val secondaryLight = Color(0xFF6A2E00)
val onSecondaryLight = Color(0xFFFFC9AA)
val secondaryContainerLight = Color(0xFFC16514)
val onSecondaryContainerLight = Color(0xFFFFFFFF)
val tertiaryLight = Color(0xFF20472D)
val onTertiaryLight = Color(0xFFB3DFBB)
val tertiaryContainerLight = Color(0xFF557E60)
val onTertiaryContainerLight = Color(0xFFFFFFFF)
val errorLight = Color(0xFF7D1800)
val onErrorLight = Color(0xFFFFC7BA)
val errorContainerLight = Color(0xFFD53D18)
val onErrorContainerLight = Color(0xFFFFFFFF)
val backgroundLight = Color(0xFFF1FADD)
val onBackgroundLight = Color(0xFF1A2210)
val surfaceLight = Color(0xFFF1FADD)
val onSurfaceLight = Color(0xFF1A2210)
val surfaceVariantLight = Color(0xFFCCDAB2)
val onSurfaceVariantLight = Color(0xFF3A422D)
val outlineLight = Color(0xFF565F48)
val outlineVariantLight = Color(0xFF6F7B5C)
val scrimLight = Color(0xFF000000)
val inverseSurfaceLight = Color(0xFF171F11)
val inverseOnSurfaceLight = Color(0xFFBFC8AD)
val inversePrimaryLight = Color(0xFFB1FAB4)
val surfaceDimLight = Color(0xFFCCDAB2)
val surfaceBrightLight = Color(0xFFF1FADD)
val surfaceContainerLowestLight = Color(0xFFFFFFFF)
val surfaceContainerLowLight = Color(0xFFEBF5D5)
val surfaceContainerLight = Color(0xFFE1EDCB)
val surfaceContainerHighLight = Color(0xFFDBE7C4)
val surfaceContainerHighestLight = Color(0xFFD5E2BC)

val primaryDark = Color(0xFFB1FAB4)
val onPrimaryDark = Color(0xFF0F5723)
val primaryContainerDark = Color(0xFF6BB071)
val onPrimaryContainerDark = Color(0xFF001905)
val secondaryDark = Color(0xFFFFA269)
val onSecondaryDark = Color(0xFF4C1F00)
val secondaryContainerDark = Color(0xFFBC5800)
val onSecondaryContainerDark = Color(0xFFFFFFFF)
val tertiaryDark = Color(0xFFEAFFEB)
val onTertiaryDark = Color(0xFF365D42)
val tertiaryContainerDark = Color(0xFFC9F6D1)
val onTertiaryContainerDark = Color(0xFF2E553A)
val errorDark = Color(0xFFFFA089)
val onErrorDark = Color(0xFF5A0E00)
val errorContainerDark = Color(0xFFD53D18)
val onErrorContainerDark = Color(0xFFFFFFFF)
val backgroundDark = Color(0xFF091003)
val onBackgroundDark = Color(0xFFFFFFFF)
val surfaceDark = Color(0xFF091003)
val onSurfaceDark = Color(0xFFFFFFFF)
val surfaceVariantDark = Color(0xFF091003)
val onSurfaceVariantDark = Color(0xFFB3BCA1)
val outlineDark = Color(0xFF8E977D)
val outlineVariantDark = Color(0xFF6E7A5A)
val scrimDark = Color(0xFF000000)
val inverseSurfaceDark = Color(0xFFF4FDDF)
val inverseOnSurfaceDark = Color(0xFF333B27)
val inversePrimaryDark = Color(0xFF1D612C)
val surfaceDimDark = Color(0xFF091003)
val surfaceBrightDark = Color(0xFF253015)
val surfaceContainerLowestDark = Color(0xFF000000)
val surfaceContainerLowDark = Color(0xFF0E1604)
val surfaceContainerDark = Color(0xFF141C08)
val surfaceContainerHighDark = Color(0xFF19220C)
val surfaceContainerHighestDark = Color(0xFF1F2910)

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) darkScheme else lightScheme,
        typography = AppTypography,
        content = content
    )
}
