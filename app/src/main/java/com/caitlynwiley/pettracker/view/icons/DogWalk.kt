package com.caitlynwiley.pettracker.view.icons

import androidx.compose.material.Icon
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

internal val DogWalkIcon: ImageVector
    get() {
        if (_dogWalk != null) {
            return _dogWalk!!
        }
        _dogWalk = materialIcon(name = "dog_walk") {
            ImageVector.Builder(
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 981f,
                viewportHeight = 920f
            ).materialPath {
                moveTo(43.8f, 13.7f)
                lineToRelative(-10.6f, 12.7f)
                lineToRelative(77.1f, 51.2f)
                curveToRelative(42.5f, 28.2f, 104.7f, 69.4f, 138.2f, 91.7f)
                curveToRelative(146.8f, 97.6f, 257.4f, 170.9f, 275f, 182.5f)
                lineToRelative(19f, 12.5f)
                lineToRelative(-6f, 2.8f)
                curveToRelative(-15.4f, 7.1f, -30.9f, 10.8f, -46.1f, 10.9f)
                curveToRelative(-4.9f, -0f, -19.1f, 0.7f, -31.5f, 1.5f)
                curveToRelative(-12.3f, 0.8f, -33.4f, 2.1f, -46.8f, 3f)
                curveToRelative(-159.7f, 9.8f, -261.5f, -3.9f, -326f, -44f)
                curveToRelative(-17.3f, -10.8f, -34f, -26.2f, -43.4f, -40.2f)
                curveToRelative(-3.8f, -5.7f, -4.5f, -6.2f, -7.8f, -6.3f)
                curveToRelative(-4.7f, -0f, -13.4f, 2.6f, -17.8f, 5.5f)
                curveToRelative(-13.1f, 8.4f, -19f, 25.4f, -14.7f, 42.5f)
                curveToRelative(10.5f, 41.9f, 60.8f, 75.8f, 149f, 100.5f)
                lineToRelative(5.9f, 1.6f)
                lineToRelative(-0.7f, 9.2f)
                curveToRelative(-3.1f, 42.8f, -12.3f, 130.1f, -18.2f, 172.1f)
                curveToRelative(-9.5f, 68.4f, -17.7f, 108.7f, -29.4f, 145.1f)
                curveToRelative(-10.5f, 32.7f, -14.3f, 53.3f, -14.3f, 77f)
                curveToRelative(0.1f, 10.3f, 0.6f, 17.5f, 1.8f, 23f)
                curveToRelative(5.7f, 26.7f, 20f, 43.6f, 40.2f, 47.4f)
                curveToRelative(8.6f, 1.7f, 15.2f, -3f, 24.5f, -17.2f)
                curveToRelative(15f, -23.1f, 36.2f, -79.1f, 56.3f, -149.2f)
                curveToRelative(8.9f, -30.7f, 23.2f, -86.4f, 25.6f, -99f)
                curveToRelative(0.7f, -3.9f, 1.5f, -7.3f, 1.9f, -7.8f)
                curveToRelative(0.3f, -0.4f, 2.3f, 7.7f, 4.4f, 18f)
                curveToRelative(18.5f, 92.2f, 32.3f, 144.5f, 48.7f, 185.1f)
                curveToRelative(19.1f, 47f, 38.4f, 70.4f, 58.4f, 70.5f)
                curveToRelative(29.7f, 0.2f, 38.2f, -42.1f, 25f, -124.8f)
                curveToRelative(-5.9f, -36.7f, -21.1f, -100.8f, -30.5f, -128.1f)
                curveToRelative(-2.9f, -8.4f, -2.9f, -8.4f, 4.3f, -8.4f)
                curveToRelative(8.7f, -0f, 27.5f, 4f, 84.2f, 17.9f)
                curveToRelative(49.1f, 12f, 68.4f, 16.3f, 78.3f, 17.6f)
                curveToRelative(6.1f, 0.8f, 7.9f, 2.2f, 8.8f, 7.1f)
                curveToRelative(1.4f, 7.6f, -1.9f, 21.2f, -17f, 69.6f)
                curveToRelative(-15.6f, 50.2f, -19.5f, 68.5f, -20.3f, 93.8f)
                curveToRelative(-0.4f, 12.9f, -0.2f, 18f, 1.1f, 23.3f)
                curveToRelative(3.2f, 13.6f, 10.6f, 23.8f, 21.1f, 29.1f)
                curveToRelative(7.5f, 3.7f, 13.4f, 4.8f, 23.2f, 4.4f)
                curveToRelative(10.1f, -0.4f, 17.1f, -3.1f, 26.1f, -10f)
                curveToRelative(11.2f, -8.6f, 24.5f, -31f, 32.7f, -54.7f)
                curveToRelative(10.3f, -30.1f, 19.2f, -81f, 21.2f, -120.9f)
                curveToRelative(0.4f, -8.1f, 1f, -12.9f, 1.5f, -12.4f)
                curveToRelative(0.5f, 0.5f, 5.6f, 13.1f, 11.3f, 28f)
                curveToRelative(23.5f, 61.5f, 39.9f, 96.4f, 58.4f, 124.4f)
                curveToRelative(14.6f, 22.1f, 27.9f, 35.6f, 42f, 42.6f)
                curveToRelative(6.3f, 3.1f, 8.7f, 3.8f, 15.2f, 4.1f)
                curveToRelative(10.2f, 0.4f, 16.7f, -1.7f, 23f, -7.6f)
                curveToRelative(16.1f, -15.1f, 15.4f, -48.8f, -2.2f, -103f)
                curveToRelative(-13.8f, -42.3f, -41.6f, -103.8f, -67.4f, -148.8f)
                lineToRelative(-8.6f, -15f)
                lineToRelative(1.5f, -3.5f)
                curveToRelative(0.7f, -1.9f, 7.3f, -17.5f, 14.6f, -34.5f)
                curveToRelative(16.9f, -39.6f, 37.8f, -91.5f, 46.8f, -116.5f)
                curveToRelative(7.1f, -19.7f, 14.2f, -42.6f, 14.2f, -46f)
                curveToRelative(0f, -4.8f, -0.2f, -4.8f, 21.3f, -2.3f)
                curveToRelative(57.4f, 6.4f, 108f, 10.7f, 118.8f, 10.1f)
                curveToRelative(6.2f, -0.3f, 7.4f, -0.8f, 14f, -5.3f)
                curveToRelative(14f, -9.6f, 37.6f, -34.9f, 50.2f, -54f)
                curveToRelative(7f, -10.6f, 10.5f, -18.1f, 11.4f, -24.1f)
                curveToRelative(0.7f, -5.2f, 0.2f, -5.8f, -16.3f, -17.8f)
                curveToRelative(-14.1f, -10.3f, -28.7f, -23.1f, -44.1f, -38.7f)
                curveToRelative(-15.2f, -15.5f, -23.3f, -24.9f, -31.3f, -36.9f)
                curveToRelative(-7.4f, -11f, -10.6f, -17.3f, -14.6f, -29.2f)
                curveToRelative(-1.8f, -5.3f, -4.6f, -11.7f, -6.3f, -14.1f)
                curveToRelative(-27.2f, -39.6f, -131.7f, -51.6f, -247.4f, -28.4f)
                lineToRelative(-11.1f, 2.3f)
                lineToRelative(10.8f, 8.9f)
                curveToRelative(19.9f, 16.6f, 36.2f, 27f, 45.4f, 29f)
                curveToRelative(2.6f, 0.5f, 3.2f, 1.1f, 3.2f, 3.2f)
                curveToRelative(0f, 12.5f, -41.9f, 58.2f, -81.2f, 88.7f)
                lineToRelative(-8.7f, 6.7f)
                lineToRelative(-86.3f, -56.2f)
                curveToRelative(-47.5f, -31f, -101.6f, -66.2f, -120.3f, -78.4f)
                curveToRelative(-18.7f, -12.2f, -97.4f, -63.4f, -174.8f, -113.9f)
                curveToRelative(-77.4f, -50.4f, -141.4f, -91.9f, -142.1f, -92.2f)
                curveToRelative(-0.7f, -0.3f, -5.6f, 4.8f, -11.8f, 12.3f)
                close()
            }
        }
        return _dogWalk!!
    }

private var _dogWalk: ImageVector? = null

@Preview
@Composable
private fun PreviewIcon() {
    Icon(imageVector = CustomIcons.DogWalk, "")
}