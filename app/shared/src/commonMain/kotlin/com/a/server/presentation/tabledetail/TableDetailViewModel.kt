package com.a.server.presentation.tabledetail

import androidx.lifecycle.ViewModel
import com.a.server.presentation.util.Effect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class TableDetailViewModel(

): ViewModel() {
    private val _state = MutableStateFlow(TableDetailState())
    val state = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.UNLIMITED)
    val effect = _effect.receiveAsFlow()

    init {

    }

    fun onAction(action: TableDetailAction) {

    }
}