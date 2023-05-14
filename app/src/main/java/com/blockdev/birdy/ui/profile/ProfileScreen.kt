package com.blockdev.birdy.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.blockdev.birdy.R

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo_blue),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .padding(top = 16.dp)
                .size(128.dp)
                .clip(CircleShape)
        )
        Text(
            text = "John Doe",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "johndoe@gmail.com",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 4.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Column(modifier = Modifier.fillMaxWidth()) {
            ProfileSetting("New Post", R.drawable.ic_new_post)
            ProfileSetting("Friend Request", R.drawable.ic_add_circle)
            ProfileSetting("Friends", R.drawable.ic_friend)
            ProfileSetting("Settings", R.drawable.ic_settings)
            ProfileSetting("About", R.drawable.ic_about)
            ProfileSetting("Log Out", R.drawable.ic_log_out)
        }
    }
}

@Composable
fun ProfileSetting(name: String, iconResId: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = name,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = name,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Preview
@Composable
fun ProfileSettingPreview() {
    ProfileSetting("Settings", R.drawable.ic_settings)
}

@Preview
@Composable
fun ProfilePreview() {
    ProfileScreen()
}
