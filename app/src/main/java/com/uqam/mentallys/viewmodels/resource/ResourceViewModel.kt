package com.uqam.mentallys.viewmodels.resource

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.uqam.mentallys.model.resource.HistoryEntry
import com.uqam.mentallys.model.resource.Resource
import com.uqam.mentallys.repository.ResourceRepositoryInterface
import com.uqam.mentallys.utils.isPointInTheSquare
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.Normalizer
import javax.inject.Inject
import kotlin.math.pow


/*
 *
 * Actually don't leak context, it's just a linter error :https://stackoverflow.com/questions/66216839/inject-context-with-hilt-this-field-leaks-a-context-object
 */
@HiltViewModel
@SuppressLint("StaticFieldLeak")
class ResourceViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val resourceRepository: ResourceRepositoryInterface,
) : ViewModel() {

    val list = MutableLiveData<List<Resource>>()
    val tagList = MutableLiveData<List<String>>()
    val history = MutableLiveData<List<Resource>>()
    private var tags: List<String>? = null

    init {
        getAllTags()
    }

    fun fetch() {
        CoroutineScope(Dispatchers.IO).launch {
            val mockData = resourceRepository.fetch()
            list.postValue(mockData)
        }
    }

    fun getHistory() {
        CoroutineScope(Dispatchers.IO).launch {
            val historyUUIDs = resourceRepository.getHistory()
            val mockData = historyUUIDs.map { x -> x.resource }
            history.postValue(mockData.reversed())
        }
    }

    fun saveInHistory(resource: Resource) {
        CoroutineScope(Dispatchers.IO).launch {
            resourceRepository.saveInHistory(HistoryEntry(resource = resource))
        }
    }

    fun getNearestPoint(position: LatLng): Resource? {
        return list.value?.minByOrNull {
            val temp = FloatArray(1)
            android.location.Location.distanceBetween(it.position.latitude,
                it.position.longitude,
                position.latitude,
                position.longitude,
                temp)
            temp.first()
        }
    }

    fun getLatLngBounds(): LatLngBounds {
        val builder: LatLngBounds.Builder = LatLngBounds.builder()
        list.value?.forEach {
            builder.include(it.position)
        }
        return builder.build()
    }

    private fun sortByDistance(mockData: List<Resource>, position: LatLng): List<Resource> {
        return mockData.sortedBy {
            (it.latitude - position.latitude).pow(2) + (it.longitude - position.longitude).pow(2)
        }
    }

    private fun getAllTags() {
        CoroutineScope(Dispatchers.IO).launch {
            if (tags == null) {
                tags = resourceRepository.fetch().asSequence().map { it.tags }.toList()
                    .flatten()
                    .distinctBy { it.uppercase() }
                    .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it })
                    .toList()
            }
            tagList.postValue(tags as List<String>)
        }
    }

    fun fetchWithFilter(
        resourceFilter: FilterViewModel.ResourceFilter,
    ) {
        if (resourceFilter.isInitialized) {
            CoroutineScope(Dispatchers.IO).launch {
                var mockData = resourceRepository.fetch()
                // Geo filtering
                mockData = applyGeoFiltering(mockData, resourceFilter)
                // Property filtering
                mockData = applyStandardFilter(mockData, resourceFilter)
                // Search string
                mockData = applyTextSearch(mockData, resourceFilter)
                // Sort by proximity
                try {
                    val bounds =
                        LatLngBounds(resourceFilter.southWestPoint, resourceFilter.northEastPoint)
                    mockData = sortByDistance(mockData, bounds.center)
                } catch (e: IllegalArgumentException) {
                    // Don't sort point when the latitude longitude property are incoherent
                }
                list.postValue(mockData)
            }
        }
    }

    private fun applyGeoFiltering(
        mockData: List<Resource>,
        resourceFilter: FilterViewModel.ResourceFilter,
    ): List<Resource> {
        return mockData.filter {
            isPointInTheSquare(resourceFilter.southWestPoint,
                it.position,
                resourceFilter.northEastPoint)
        }
    }

    private fun applyStandardFilter(
        mockData: List<Resource>,
        resourceFilter: FilterViewModel.ResourceFilter,
    ): List<Resource> {
        var temp = mockData.filter { resourceFilter.categories.contains(it.category) }
        temp = temp.filter { resourceFilter.costs.contains(it.cost) }
        temp = temp.filter { resourceFilter.clients.any { x -> it.clients.any { y -> y == x } } }
        temp =
            temp.filter {
                when (resourceFilter.tags.isNotEmpty()) {
                    true -> {
                        resourceFilter.tags.any { x ->
                            it.tags.any { y ->
                                Normalizer.normalize(y, Normalizer.Form.NFD)
                                    .uppercase() == Normalizer.normalize(x, Normalizer.Form.NFD)
                                    .uppercase()
                            }
                        }
                    }
                    false -> true
                }
            }
        temp =
            temp.filter { resourceFilter.activities.any { x -> it.activities.any { y -> y == x } } }
        temp =
            temp.filter { resourceFilter.modalities.any { x -> it.modalities.any { y -> y == x } } }
        temp =
            temp.filter { resourceFilter.languages.any { x -> it.languages.any { y -> y == x } } }
        return temp
    }


    private fun applyTextSearch(
        mockData: List<Resource>,
        resourceFilter: FilterViewModel.ResourceFilter,
    ): List<Resource> {
        var temp = mockData
        if (resourceFilter.searchString != "") {
            for (word in resourceFilter.searchString.split(" ")) {
                temp = temp.filter {
                    if (it.index.isEmpty()) {
                        it.index = listOf(
                            it.name,
                            it.description.toString(),
                            it.address,
                            it.postalCode,
                            it.city,
                            it.region,
                            it.country,
                            context.getText(it.cost.getTextId()).toString(),
                        )
                        /*
                        it.index = it.index + it.tags + it.activities.map { activity ->
                            context.getText(activity.getTextId()).toString()
                        } + it.languages.map { language ->
                            context.getText(language.getTextId()).toString()
                        } + it.clients.map { client ->
                            context.getText(client.getTextId()).toString()
                        } + it.modalities.map { modality ->
                            context.getText(modality.getTextId()).toString()
                        }
                        */
                        it.index = it.index.map { s -> normalizeString(s) }
                    }
                    it.index.any { entry -> entry.contains(normalizeString(word)) }
                }
            }
        }
        return temp
    }

    private fun normalizeString(s: String): String {
        val temp = Normalizer.normalize(s, Normalizer.Form.NFD)
        return "\\p{InCombiningDiacriticalMarks}+".toRegex().replace(temp, "").uppercase()
    }


}