package com.highboy.gomantle.ui.state

import com.highboy.gomantle.data.ViewType

data class GomantleUiState (
    val currentViewType: ViewType = ViewType.Game
)