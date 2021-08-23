package com.caitlynwiley.pettracker.ui.icons

import androidx.compose.material.Icon
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

internal val DogPoopIcon: ImageVector
    get() {
        if (_dogPoop != null) {
            return _dogPoop!!
        }
        _dogPoop = materialIcon(name = "dog_poop") {
            ImageVector.Builder(
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 200f,
                viewportHeight = 200f
            ).materialPath {
                moveTo(102.2f, 41.6f)
                curveToRelative(-1.9f, 6.5f, -3f, 8.3f, -7.5f, 12.8f)
                curveToRelative(-2.9f, 2.8f, -10.2f, 8f, -16.2f, 11.5f)
                curveToRelative(-17.5f, 10.3f, -23.3f, 17.6f, -22.3f, 28.2f)
                curveToRelative(0.9f, 9.4f, 7.9f, 16.1f, 20.1f, 19.3f)
                curveToRelative(8.2f, 2.2f, 29.2f, 3f, 41.4f, 1.7f)
                curveToRelative(17.2f, -1.8f, 26.6f, -6.3f, 29.8f, -14.4f)
                curveToRelative(3.7f, -9.2f, -1.2f, -18.9f, -12.1f, -23.8f)
                curveToRelative(-3.5f, -1.6f, -6.8f, -2.9f, -7.3f, -2.9f)
                curveToRelative(-0.5f, -0f, -1.4f, 1.4f, -2.1f, 3f)
                curveToRelative(-1.8f, 4.4f, -10.2f, 11.5f, -15.7f, 13.4f)
                curveToRelative(-6.3f, 2.2f, -14.2f, 2.1f, -15f, -0f)
                curveToRelative(-1.1f, -3.1f, 1.2f, -4.9f, 7f, -5.5f)
                curveToRelative(7.3f, -0.8f, 12.5f, -3.6f, 16.2f, -8.8f)
                curveToRelative(2.9f, -4.1f, 3f, -4.8f, 3f, -14.4f)
                curveToRelative(0f, -9.6f, -0.2f, -10.5f, -3.1f, -15.7f)
                curveToRelative(-3f, -5.2f, -10.2f, -12f, -12.8f, -12f)
                curveToRelative(-0.6f, -0f, -2.2f, 3.4f, -3.4f, 7.6f)
                close()
                moveTo(44.5f, 102.6f)
                curveToRelative(-10.5f, 7f, -15.4f, 14.1f, -16.2f, 23.6f)
                curveToRelative(-0.8f, 8.2f, 1.4f, 13.5f, 8.4f, 19.8f)
                curveToRelative(14.1f, 12.9f, 37.3f, 18.8f, 70.3f, 17.7f)
                curveToRelative(21.9f, -0.7f, 33.4f, -3f, 47f, -9.5f)
                curveToRelative(7.3f, -3.6f, 15.3f, -11.7f, 17.4f, -17.8f)
                curveToRelative(4.1f, -12f, 0.1f, -25.5f, -9.2f, -31.6f)
                curveToRelative(-5.9f, -3.9f, -7.1f, -4.1f, -8.2f, -1.4f)
                curveToRelative(-1.3f, 3.6f, -6.8f, 9.4f, -11.5f, 12.1f)
                curveToRelative(-8.7f, 5.1f, -18.1f, 6.8f, -39.5f, 6.8f)
                curveToRelative(-20.8f, 0.1f, -28.6f, -1.1f, -37.3f, -5.4f)
                curveToRelative(-5.4f, -2.7f, -11.3f, -8.4f, -13.8f, -13.2f)
                curveToRelative(-1.1f, -2f, -2.3f, -3.7f, -2.7f, -3.7f)
                curveToRelative(-0.4f, -0f, -2.5f, 1.2f, -4.7f, 2.6f)
                close()
            }
        }
        return _dogPoop!!
    }

private var _dogPoop: ImageVector? = null

@Preview
@Composable
private fun PreviewIcon() {
    Icon(imageVector = CustomIcons.DogPoop, "")
}