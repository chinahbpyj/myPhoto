package com.pyj.myphoto.viewmodel.you

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pyj.myphoto.api.YouxkApi
import com.pyj.myphoto.data.YouPhoto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class YouViewModel : ViewModel() {
    private val viewModelState = MutableStateFlow(YouState())

    private var photos: List<YouPhoto>? = listOf()

    val uiState = viewModelState
        .map(YouState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    fun settingUpdateValue(key: String, value: String) {
        viewModelState.update {
            it.copy(
                keyName = key,
                valueName = value,
            )
        }
    }

    fun request(value: String) {
        viewModelState.update { it.copy(request = true) }
        loadData(value)
    }

    fun refresh(value: String) {
        viewModelState.update { it.copy(refreshing = true) }
        loadData(value)
    }

    private fun loadData(value: String) {
        val call = YouxkApi.createPxApi().dataList("gallery", value)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                viewModelScope.launch {
                    val inputStream: InputStream? = response.body()?.byteStream()

                    val result: String = inToString(inputStream)

                    val resultList = parsePhoto(result)
                    if (resultList != null) {
                        photos = resultList

                        viewModelState.update {
                            it.copy(
                                request = false,
                                photos = photos,
                                refreshing = false
                            )
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })
    }

    fun loadMore() {
        viewModelState.update { it.copy(loading = true) }

        val list = mutableListOf<YouPhoto>()
        list.addAll(photos!!)
        viewModelScope.launch {
            val call = YouxkApi.createPxApi().dataList("gallery", "latest")
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val inputStream: InputStream? = response.body()?.byteStream()

                    val result: String = inToString(inputStream)

                    val resultList = parsePhoto(result)
                    if (resultList != null) {
                        list.addAll(resultList)
                        photos = list

                        viewModelState.update {
                            it.copy(photos = photos, loading = false)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                }
            })

        }
    }

    fun inToString(inputStream: InputStream?): String {
        if (inputStream == null) {
            return ""
        }
        var input = inputStream
        val res = StringBuffer()
        // 把字节流转化为字符流
        val isr = InputStreamReader(input)
        // 普通的Reader读取输入内容时依然不太方便，可以将普通的
        // Reader再次包装成BufferedReader,利用BufferReader的readLine()
        // 方法可以一次读取一行内容
        var read: BufferedReader? = BufferedReader(isr)
        try {
            var line: String?
            line = read!!.readLine()
            while (line != null) {
                res.append("$line<br>")
                line = read.readLine()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (null != isr) {
                    isr.close()
                    isr.close()
                }
                if (null != read) {
                    read.close()
                    read = null
                }
                if (null != input) {
                    input.close()
                    input = null
                }
            } catch (e: IOException) {
            }
        }
        return res.toString()
    }

    fun parsePhoto(result: String?): List<YouPhoto>? {
        if (result.isNullOrEmpty()) {
            return null
        }

        val photoss: MutableList<YouPhoto> = ArrayList()
        val doc: Document = Jsoup.parse(result)
        val elementDesc: Elements = doc.select("div.descbox")
        val elementImg: Elements = doc.select("img")
        for (i in elementDesc.indices) {
            val photo = YouPhoto()
            val desc = elementDesc[i].getElementsByTag("a")
            photo.url = (desc[0].attr("href"))
            photo.desc = (desc[0].text())
            val img = elementImg[i + 2].getElementsByTag("img")
            photo.pic = (img[0].attr("src"))

            //获取网络图片的宽高
            val style = img[0].attr("style")
            val st = style.split(";".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val w = st[0]
            val h = st[1]
            val wi = w.split(": ".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val widt = wi[1] //474px
            val lastw = widt.indexOf("p")
            val width = widt.substring(0, lastw)
            val he = h.split(": ".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val heigh = he[1] //327px
            val lasth = heigh.indexOf("p")
            val height = heigh.substring(0, lasth)
            photo.heigh = (height)
            photo.width = (width)
            photoss.add(photo)
        }
        return photoss
    }

    companion object {
        fun provideFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return YouViewModel() as T
            }
        }
    }
}
