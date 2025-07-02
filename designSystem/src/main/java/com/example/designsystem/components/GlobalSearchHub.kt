package com.example.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GlobalSearchHub(
    name: String,
    description: String,
    @DrawableRes icon: Int,
    brush: Brush,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(brush = brush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.height(40.dp),
                contentScale = ContentScale.FillHeight
            )
            Column() {
                Text(
                    text = name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = Color.White.copy(alpha = 0.87f)
                )
                Text(
                    text = description,
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp,
                    lineHeight = 16.sp,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxHeight(0.56f)
                .fillMaxWidth(0.35f)
                .blur(56.dp, BlurredEdgeTreatment.Unbounded)
                .background(color = Color.White.copy(alpha = 0.5f), CircleShape)
                .align(Alignment.TopEnd)
        )
    }
}