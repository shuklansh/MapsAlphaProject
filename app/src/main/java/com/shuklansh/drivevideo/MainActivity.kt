package com.shuklansh.drivevideo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import com.shuklansh.drivevideo.ui.theme.MapsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var link = "https://drive.google.com/u/0/uc?id=1MR35U8L-Fz2OkXTJNyg2JpCL2sIzUs_Q&export=download"
//            var link = "https://drive.google.com/file/d/1PkLcUy6amFKDocQt_cGdUjop7PuxpsXN/view?usp=sharing"
//            var link = "https://drive.google.com/file/d/1PQft_0uE0u_E5KmYT4le-XQzlR_4CABq/view"
//            var link = "https://drive.google.com/u/0/uc?id=1PQft_0uE0u_E5KmYT4le-XQzlR_4CABq&export=download"
//            var link = "https://drive.google.com/uc?id=1PQft_0uE0u_E5KmYT4le-XQzlR_4CABq"

//            var link= "https://rr5---sn-ci5gup-a3v6.googlevideo.com/videoplayback?expire=1688348929&ei=odShZOHBCdGVkgbh-7_ACA&ip=102.129.232.155&id=o-ACGxuhcgcjTTxiixos1S1inf8mL5tXLDocM2pwe6t8Mu&itag=22&source=youtube&requiressl=yes&spc=Ul2Sq2AORDr9ALRukLedITb5GMSjhMM_TWwDryw7zQ&vprv=1&svpuc=1&mime=video%2Fmp4&ns=pY8Xlyetzj4oEVjZGjO_zpYO&cnr=14&ratebypass=yes&dur=2821.944&lmt=1688309902184910&fexp=24007246,51000012,51000023&c=WEB&txp=3308224&n=-006KslvHx0dOA&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cspc%2Cvprv%2Csvpuc%2Cmime%2Cns%2Ccnr%2Cratebypass%2Cdur%2Clmt&sig=AOq0QJ8wRQIhAJNzOSq79LcULfOCvG3wqP4yEeO_3CHFitgk5ka7hKwKAiAZ18fe9URFbuQZ-DU5XXePwwDeOVoQ_LRaN_MKQDxN-w%3D%3D&title=SIDEMEN%20HIDE%20%26%20SEEK%20IN%20A%20ZOO&redirect_counter=1&rm=sn-a5meel7e&req_id=bd0bab27bf87a3ee&cms_redirect=yes&cmsv=e&hcs=sd&ipbypass=yes&mh=eX&mip=106.214.246.41&mm=31&mn=sn-ci5gup-a3v6&ms=au&mt=1688329300&mv=m&mvi=5&pcm2cms=yes&pl=20&rmhost=rr3---sn-ci5gup-a3v6.googlevideo.com&smhost=rr4---sn-ci5gup-a3ve.googlevideo.com&lsparams=hcs,ipbypass,mh,mip,mm,mn,ms,mv,mvi,pcm2cms,pl,rmhost,smhost&lsig=AG3C_xAwRAIgWOEoeE6yiyPEAYSPK4gCIhH66fIy7kIoo43bOPZ-KnwCIBaddOq-HsIsHsPBF5PiMTPio2OXcC4K1eFKNkXs1ool"

            var exoPlayer = remember{
                ExoPlayer.Builder(applicationContext).build()
            }


            MapsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(Modifier.fillMaxSize()){
                        AndroidView(factory = {ctx->
                            PlayerView(ctx).apply {
//                                Log.d("@@@@", linkConverter(link))
//                                var mediaItem = MediaItem.fromUri(linkConverter(link))
                                var mediaItem = MediaItem.fromUri(linkConverter(link))
                                exoPlayer.setMediaItem(mediaItem)
                                exoPlayer.prepare()
                                player = exoPlayer
                                exoPlayer.playWhenReady = true
                                exoPlayer.play()
                            }

                        } )
                    }
                }
            }
        }
    }
}

fun linkConverter(s:String) : String{
    var scopy = s
    if(scopy.contains("file/d/")){
        var new = if(s.contains("?usp=sharing")){s.replace("/view?usp=sharing","")}else{s.replace("/view","")}
        var newlink = new.replace("file/d/","uc?id=")
        return newlink
    }else{
        return scopy
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MapsAppTheme {
        Greeting("Android")
    }
}


