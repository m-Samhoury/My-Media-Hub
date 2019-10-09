package com.moustafa.mymediahub

import android.accounts.NetworkErrorException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.moustafa.mymediahub.features.imagegallerylistscreen.MyHubGalleryViewModel
import com.moustafa.mymediahub.models.PhotoInfo
import com.moustafa.mymediahub.repository.Repository
import com.moustafa.mymediahub.repository.network.StateMonitor
import com.moustafa.mymediahub.utils.CoroutineRule
import com.moustafa.mymediahub.utils.LiveDataTestUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.withContext
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import java.io.File


/**
 * Some tests to make sure the RecipeViewModel is emitting the right states for the UI
 * to be then rendered
 */
@UseExperimental(ExperimentalCoroutinesApi::class)
class MyHubGalleryScreenTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    internal lateinit var myHubGalleryViewModel: MyHubGalleryViewModel

    private val currentViewModelState
        get() = LiveDataTestUtil.getValue(myHubGalleryViewModel.stateLiveData)

    @Test
    fun `get images list triggers loading state`() = coroutinesTestRule.runBlockingTest {
        val repository = object : Repository {
            override suspend fun fetchHubImages(onError: suspend (Exception) -> Unit): List<PhotoInfo>? {
                return emptyList()
            }

            override suspend fun uploadImage(
                bitmap: File,
                onError: suspend (Exception) -> Unit
            ): Boolean? = TODO()
        }
        coroutinesTestRule.pauseDispatcher()

        myHubGalleryViewModel = MyHubGalleryViewModel(repository)


        assertThat(currentViewModelState.imagesStateMonitor is StateMonitor.Loading).isTrue()
        coroutinesTestRule.resumeDispatcher()

    }

    @Test
    fun `get images list and return success`() = coroutinesTestRule.runBlockingTest {
        val repository = object : Repository {
            override suspend fun fetchHubImages(onError: suspend (Exception) -> Unit): List<PhotoInfo>? {
                return listOf(
                    PhotoInfo(imageUrl = ""),
                    PhotoInfo(imageUrl = ""),
                    PhotoInfo(imageUrl = ""),
                    PhotoInfo(imageUrl = ""),
                    PhotoInfo(imageUrl = "5")
                )
            }

            override suspend fun uploadImage(
                bitmap: File,
                onError: suspend (Exception) -> Unit
            ): Boolean? = TODO()
        }

        coroutinesTestRule.pauseDispatcher()

        myHubGalleryViewModel = MyHubGalleryViewModel(repository)

        assertThat(currentViewModelState.imagesStateMonitor is StateMonitor.Loading).isTrue()

        coroutinesTestRule.resumeDispatcher()

        assertThat(currentViewModelState.imagesStateMonitor is StateMonitor.Loaded).isTrue()
        assertThat((currentViewModelState.imagesStateMonitor as StateMonitor.Loaded).result)
            .hasSize(5)
    }

    @Test
    fun `get images list and return failure`() = coroutinesTestRule.runBlockingTest {
        val repository = object : Repository {
            override suspend fun fetchHubImages(onError: suspend (Exception) -> Unit): List<PhotoInfo>? {
                withContext(Dispatchers.Main) {
                    onError.invoke(NetworkErrorException("No internet"))
                }
                return null
            }

            override suspend fun uploadImage(
                bitmap: File,
                onError: suspend (Exception) -> Unit
            ): Boolean? = TODO()
        }
        coroutinesTestRule.pauseDispatcher()

        myHubGalleryViewModel = MyHubGalleryViewModel(repository)


        assertThat(currentViewModelState.imagesStateMonitor is StateMonitor.Loading).isTrue()

        coroutinesTestRule.resumeDispatcher()

        assertThat(currentViewModelState.imagesStateMonitor is StateMonitor.Failed).isTrue()
        assertThat((currentViewModelState.imagesStateMonitor as StateMonitor.Failed).failed)
            .isInstanceOf(NetworkErrorException::class.java)
    }

    @Test
    fun `upload image triggers loading state`() = coroutinesTestRule.runBlockingTest {
        val repository = object : Repository {
            override suspend fun fetchHubImages(onError: suspend (Exception) -> Unit): List<PhotoInfo>? {
                return emptyList()
            }

            override suspend fun uploadImage(
                bitmap: File,
                onError: suspend (Exception) -> Unit
            ): Boolean? = true
        }

        myHubGalleryViewModel = MyHubGalleryViewModel(repository)

        myHubGalleryViewModel.uploadImage(File(""))
        coroutinesTestRule.pauseDispatcher()

        assertThat(currentViewModelState.imagesStateMonitor is StateMonitor.Loading).isTrue()
        coroutinesTestRule.resumeDispatcher()
    }

    @Test
    fun `upload image success triggers fetch images function`() =
        coroutinesTestRule.runBlockingTest {
            val repository = object : Repository {
                override suspend fun fetchHubImages(onError: suspend (Exception) -> Unit): List<PhotoInfo>? {
                    return emptyList()
                }

                override suspend fun uploadImage(
                    file: File,
                    onError: suspend (Exception) -> Unit
                ): Boolean? {
                    return true
                }
            }

            myHubGalleryViewModel = spy(MyHubGalleryViewModel(repository))

            myHubGalleryViewModel.uploadImage(File(""))
            coroutinesTestRule.pauseDispatcher()

            verify(myHubGalleryViewModel).fetchGalleryImages()
            coroutinesTestRule.resumeDispatcher()
        }
}