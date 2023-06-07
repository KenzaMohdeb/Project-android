package com.uqam.mentallys.viewModel

import android.content.Context
import com.uqam.mentallys.model.resource.*
import com.uqam.mentallys.repository.FakeResourceRepository
import com.uqam.mentallys.repository.ResourceRepositoryInterface
import com.uqam.mentallys.testUtils.InstantExecutorExtension
import com.uqam.mentallys.viewmodels.resource.FilterViewModel
import com.uqam.mentallys.viewmodels.resource.ResourceViewModel
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@ExtendWith(InstantExecutorExtension::class)
class ResourceViewModelTest {

    private lateinit var resourceViewModel: ResourceViewModel

    @BeforeEach
    fun setUp() {
        val context: Context = mock(Context::class.java)
        `when`(context.getText(any(Int::class.java))).thenReturn("placeholder")
        `when`(context.getText(Language.FRENCH.getTextId())).thenReturn("Français")
        resourceViewModel =
            ResourceViewModel(context, FakeResourceRepository() as ResourceRepositoryInterface)
    }

    //@Test
    fun `test resource fetching`() {
        resourceViewModel.fetch()
        val expectedSize = 180
        val resultSize = resourceViewModel.list.value?.size
        assert(expectedSize == resultSize)
    }

    //@Test
    fun `test resource getAllTag`() {
        resourceViewModel.fetch()
        val expectedSize = 2
        resourceViewModel.list.observeForever {
            val resultSize = it.size
            assert(expectedSize == resultSize)
        }
    }

    //@Test
    fun `test resource filter empty`() {
        val expectedSize = 180
        val filter = FilterViewModel.ResourceFilter(resourceViewModel)
        resourceViewModel.fetchWithFilter(filter)
        resourceViewModel.list.observeForever {
            val resultSize = it.size
            assert(expectedSize == resultSize)
        }
    }

    //@Test
    fun `test resource filter string (name)`() {
        val expectedSize = 30
        val filter = FilterViewModel.ResourceFilter(resourceViewModel)
        filter.searchString = "TEST STR"
        resourceViewModel.fetchWithFilter(filter)
        resourceViewModel.list.observeForever {
            val resultSize = it.size
            assert(expectedSize == resultSize)
        }
    }

    //@Test
    fun `test resource filter string (postal code)`() {
        val expectedSize = 30
        val filter = FilterViewModel.ResourceFilter(resourceViewModel)
        filter.searchString = "J3H"
        resourceViewModel.fetchWithFilter(filter)
        resourceViewModel.list.observeForever {
            val resultSize = it.size
            assert(expectedSize == resultSize)
        }
    }

    //@Test
    fun `test resource filter string (non text property)`() {
        val expectedSize = 150
        val filter = FilterViewModel.ResourceFilter(resourceViewModel)
        filter.searchString = "Français"
        resourceViewModel.fetchWithFilter(filter)
        resourceViewModel.list.observeForever {
            val resultSize = it.size
            assert(expectedSize == resultSize)
        }
    }

    //@Test
    fun `test resource filter category`() {
        val expectedSize = 120
        val filter = FilterViewModel.ResourceFilter(resourceViewModel)
        filter.categories = listOf(Category.PUBLIC, Category.PRIVATE)
        resourceViewModel.fetchWithFilter(filter)
        resourceViewModel.list.observeForever {
            val resultSize = it.size
            assert(expectedSize == resultSize)
        }
    }

    //@Test
    fun `test resource filter cost`() {
        val expectedSize = 120
        val filter = FilterViewModel.ResourceFilter(resourceViewModel)
        filter.costs = listOf(Cost.REDUCED)
        resourceViewModel.fetchWithFilter(filter)
        resourceViewModel.list.observeForever {
            val resultSize = it.size
            assert(expectedSize == resultSize)
        }
    }

    //@Test
    fun `test resource filter client`() {
        val expectedSize = 150
        val filter = FilterViewModel.ResourceFilter(resourceViewModel)
        filter.clients = listOf(Client.FAMILY, Client.TEENAGER)
        resourceViewModel.fetchWithFilter(filter)
        resourceViewModel.list.observeForever {
            val resultSize = it.size
            assert(expectedSize == resultSize)
        }
    }

    //@Test
    fun `test resource filter tag`() {
        val expectedSize = 150
        val filter = FilterViewModel.ResourceFilter(resourceViewModel)
        filter.tags = listOf("Dépression")
        resourceViewModel.fetchWithFilter(filter)
        resourceViewModel.list.observeForever {
            val resultSize = it.size
            assert(expectedSize == resultSize)
        }
    }

    //@Test
    fun `test resource filter activities`() {
        val expectedSize = 120
        val filter = FilterViewModel.ResourceFilter(resourceViewModel)
        filter.activities = listOf(Activity.FAMILY_DOCTOR, Activity.SOCIAL_WORKER)
        resourceViewModel.fetchWithFilter(filter)
        resourceViewModel.list.observeForever {
            val resultSize = it.size
            assert(expectedSize == resultSize)
        }
    }

    //@Test
    fun `test resource filter modalities`() {
        val expectedSize = 150
        val filter = FilterViewModel.ResourceFilter(resourceViewModel)
        filter.modalities = listOf(Modality.ON_SITE)
        resourceViewModel.fetchWithFilter(filter)
        val obs = resourceViewModel.list.observeForever {
            val resultSize = it.size
            assert(expectedSize == resultSize)
        }
    }

    //@Test
    fun `test resource filter language`() {
        val expectedSize = 150
        val filter = FilterViewModel.ResourceFilter(resourceViewModel)
        filter.languages = listOf(Language.FRENCH)
        resourceViewModel.fetchWithFilter(filter)
        resourceViewModel.list.observeForever {
            val resultSize = it.size
            assert(expectedSize == resultSize)
        }
    }

}