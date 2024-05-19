package com.example.livealbum

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.livealbum.ui.theme.LiveAlbumTheme
import com.example.livealbum.images
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LiveAlbumTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LivePhotoAlbumApp()
                }
            }
        }
    }
}


@Composable
fun VideoPlayer(rawResourceId:Int) {
    val uri = Uri.parse("android.resource://com.example.livealbum/$rawResourceId")
    var isPlaying by remember{mutableStateOf(false)}
    Surface(color = Color.White,
        modifier = Modifier
            .padding(20.dp)
            .clickable { isPlaying = isPlaying.not() }
    ) {
        AndroidView(
            modifier =Modifier.background(color = Color.White),

            factory = { context ->

                val videoView = VideoView(context)
                videoView.setVideoURI(uri)
                videoView.setOnPreparedListener { mediaPlayer ->
                    mediaPlayer.isLooping = false
                    mediaPlayer.seekTo(1)
                    mediaPlayer.setVolume(0.3f, 0.3f)
                    if (isPlaying) {
                        mediaPlayer.start()
                    }
                    }
                videoView.setOnCompletionListener {
                    mediaPlayer->
                    mediaPlayer.seekTo(1)
                    isPlaying = false
                }
                videoView
            }, update = { videoView ->
                videoView.setVideoURI(uri)
                if (isPlaying && !videoView.isPlaying) {
                    videoView.start()
                }
            }
        )
    }
}
@Composable
fun ImageDisplayer(videoClip: Int){
    Surface(color = Color.White,
        modifier = Modifier
            .padding(20.dp)
    )
    {
        Image(
            painterResource(R.drawable.img_sample),
            null
        )
    }
}
@Composable
fun PhotoWall(videoClip:Int,isLive:Boolean){
    Surface(
        modifier = Modifier
            .padding(horizontal = 20.dp),
        shadowElevation = 50.dp,
    )
    {
        if (isLive) {
            VideoPlayer(videoClip)
        }
        else{
            ImageDisplayer(videoClip)
        }

    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PhotoDescription(modifier:Modifier=Modifier,videoTitle:String,videoPlace:String,videoDate:LocalDate){
    Surface(
        color = Color(0xFFD5D4D4),
        modifier = modifier
            .height(100.dp)
            .width(350.dp)) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                videoTitle,
                fontFamily = FontFamily(Font(R.font.roboto_light)),
                fontSize = 25.sp

            )
            Row {
                Text(
                    videoPlace,
                    fontFamily = FontFamily(Font(R.font.roboto_bold)),
                    fontSize = 15.sp
                )
                Spacer(modifier= Modifier.width(5.dp))
                Text(
                    videoDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", java.util.Locale("pl"))),
                    fontFamily = FontFamily(Font(R.font.roboto_light)),
                    fontSize = 15.sp
                )
            }
        }
    }

}
@Composable
fun NavigationButtons(prevLambda:()->Unit,nextLambda:()->Unit){
    val buttonModifier = Modifier
        .padding(10.dp)
        .width(150.dp)
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ){
        Button(
            onClick = prevLambda,
            modifier = buttonModifier
        ){
            Text(text = "Previous")
        }

        Button(
            onClick = nextLambda,
            modifier = buttonModifier
        ){
            Text(text = "Next")
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun LivePhotoAlbumApp(){
    var clipNumber by remember {
        mutableIntStateOf(0)
    }
    val videoClip = images[clipNumber].resource
    val videoTitle = images[clipNumber].description
    val videoPlace = images[clipNumber].location
    val videoDate = images[clipNumber].date
    val isLive = images[clipNumber].live
    val last = images.lastIndex

    Column(
        modifier=Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom

    ) {

        Column(modifier= Modifier
            .weight(0.75f)
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            PhotoWall(videoClip,isLive)
        }

        Column(modifier= Modifier
            .weight(0.25f)
            .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            PhotoDescription(Modifier.padding(10.dp),videoTitle,videoPlace,videoDate)

            NavigationButtons(
                {
                    if( clipNumber==0)
                    {clipNumber=last}
                    else {clipNumber--}
                },
                {
                    if( clipNumber==last) clipNumber=0
                    else clipNumber++
                }
            )
            Spacer(modifier=Modifier.height(5.dp))
        }
    }
}