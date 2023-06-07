package com.uqam.mentallys.viewmodels.resource

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.google.android.gms.maps.model.LatLng
import com.uqam.mentallys.model.resource.*
import com.uqam.mentallys.repository.ResourceRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/*
 *
 * Actually don't leak context, it's just a linter error :https://stackoverflow.com/questions/66216839/inject-context-with-hilt-this-field-leaks-a-context-object
 */
@HiltViewModel
@SuppressLint("StaticFieldLeak")
class FilterViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    resourceRepository: ResourceRepositoryInterface,
) : ViewModel(), ViewModelStoreOwner {

    private val resourceViewModel = ResourceViewModel(context, resourceRepository)
    val filter: MutableLiveData<ResourceFilter> = MutableLiveData(ResourceFilter(resourceViewModel))

    class ResourceFilter(private val resourceViewModel: ResourceViewModel) {
        // Keep the original selection made by the user so it can be restored
        // Used to evaluate if filter are active
        // https://stackoverflow.com/questions/61459289/how-to-skip-defined-getters-or-setters-in-kotlin
        var isInitialized = false
        var selectedCategories: List<Category> = listOf()
        var selectedCosts: List<Cost> = listOf()
        var selectedClients: List<Client> = listOf()
        var selectedTags: List<String> = listOf()
        var selectedActivities: List<Activity> = listOf()
        var selectedModalities: List<Modality> = listOf()
        var selectedLanguages: List<Language> = listOf()
        var southWestPoint: LatLng = LatLng(-90.0, -180.0)
            set(value) {
                field = value
                isInitialized = true
            }
        var northEastPoint: LatLng = LatLng(-90.0, -180.0)
            set(value) {
                field = value
                isInitialized = true
            }
        var searchString: String = ""
        var categories: List<Category>
            get() {
                return if (selectedCategories == listOf<Category>()) {
                    Category.values().toList()
                } else {
                    selectedCategories
                }
            }
            set(value) {
                selectedCategories = value
            }
        var costs: List<Cost>
            get() {
                return if (selectedCosts == listOf<Category>()) {
                    Cost.values().toList()
                } else {
                    selectedCosts
                }
            }
            set(value) {
                selectedCosts = value

            }
        var clients: List<Client>
            get() {
                return if (selectedClients == listOf<Client>()) {
                    Client.values().toList()
                } else {
                    selectedClients
                }
            }
            set(value) {
                selectedClients = value
            }
        var tags: List<String>
            get() {
                return if (selectedTags == listOf<String>()) {
                    if (resourceViewModel.tagList.value == null){
                        selectedTags
                    } else {
                        resourceViewModel.tagList.value!!
                    }
                } else {
                    selectedTags
                }
            }
            set(value) {
                selectedTags = value
            }
        var activities: List<Activity>
            get() {
                return if (selectedActivities == listOf<Activity>()) {
                    Activity.values().toList()
                } else {
                    selectedActivities
                }
            }
            set(value) {
                selectedActivities = value
            }
        var modalities: List<Modality>
            get() {
                return if (selectedModalities == listOf<Modality>()) {
                    Modality.values().toList()
                } else {
                    selectedModalities
                }
            }
            set(value) {
                selectedModalities = value
            }
        var languages: List<Language>
            get() {
                return if (selectedLanguages == listOf<Language>()) {
                    Language.values().toList()
                } else {
                    selectedLanguages
                }
            }
            set(value) {
                selectedLanguages = value
            }

        fun isThereActiveAdvancedFilter(): Boolean {
            //_searchString == "" && _categories == listOf<ResourceCategory>()
            return !(selectedLanguages.isEmpty() &&
                    selectedModalities.isEmpty() &&
                    selectedActivities.isEmpty() &&
                    selectedTags.isEmpty() &&
                    selectedClients.isEmpty() &&
                    selectedCosts.isEmpty())
        }

        fun isThereAnyActiveFilter(): Boolean {
            //_searchString == "" && _categories == listOf<ResourceCategory>()
            return isThereActiveAdvancedFilter() || (searchString != "") || selectedCategories.isNotEmpty()
        }


        fun reset() {
            searchString = ""
            selectedCategories = listOf()
            selectedCosts = listOf()
            selectedClients = listOf()
            selectedTags = listOf()
            selectedActivities = listOf()
            selectedModalities = listOf()
            selectedLanguages = listOf()
        }

        fun clone(): ResourceFilter {
            val temp = ResourceFilter(resourceViewModel)
            temp.searchString = this.searchString
            temp.categories = this.selectedCategories
            temp.costs = this.selectedCosts
            temp.clients = this.selectedClients
            temp.tags = this.selectedTags
            temp.activities = this.selectedActivities
            temp.modalities = this.selectedModalities
            temp.languages = this.selectedLanguages
            return temp
        }


    }

    override fun getViewModelStore(): ViewModelStore {
        return ViewModelStore()
    }

}