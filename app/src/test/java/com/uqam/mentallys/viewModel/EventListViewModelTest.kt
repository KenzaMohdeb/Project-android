package com.uqam.mentallys.viewModel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.uqam.mentallys.R
import com.uqam.mentallys.model.Event
import com.uqam.mentallys.model.EventType
import com.uqam.mentallys.model.EventTypeModels
import com.uqam.mentallys.repository.FakeEventRepository
import com.uqam.mentallys.utils.UUIDConverter
import com.uqam.mentallys.view.ui.pathway.eventsList.EventListViewModel
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EventListViewModelTest {

    private lateinit var listViewModelTest: EventListViewModel

    @BeforeEach
    fun setUp() {
        listViewModelTest = EventListViewModel(repository = FakeEventRepository())
    }

    @Test
    fun `test event flow list`() = runBlocking {
        listViewModelTest.eventsFlow.test {
            val events = awaitItem()
            assertThat(events).isEqualTo(
                mutableListOf(
                    Event(
                        id = UUIDConverter().uuidFromString("c68b49b4-5286-4889-a10d-bb4dbc66949e"),
                        name = EventType(
                            typeId = UUIDConverter().uuidFromString("4c77a37d-6154-48d6-a376-e6d8f3a13a47"),
                            typeName = EventTypeModels().maintenance,
                            icon = R.drawable.entretien
                        ),
                        startDate = 1649828200000,
                        endDate = 1664636400000
                    )
                )
            )
            cancelAndConsumeRemainingEvents()
        }
    }
}