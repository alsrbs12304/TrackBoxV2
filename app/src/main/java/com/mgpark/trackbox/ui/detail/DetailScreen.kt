package com.mgpark.trackbox.ui.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mgpark.trackbox.core.util.formatKrFull
import com.mgpark.trackbox.core.util.formatKrShort
import com.mgpark.trackbox.domain.model.Tracking
import com.mgpark.trackbox.domain.model.TrackingDetail
import com.mgpark.trackbox.domain.model.TrackingProgress
import com.mgpark.trackbox.domain.model.TrackingState
import com.mgpark.trackbox.ui.common.StatePill
import com.mgpark.trackbox.ui.common.label
import com.mgpark.trackbox.ui.common.palette

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    onBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.deleted) { if (state.deleted) onBack() }
    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.consumeError()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "배송 상세",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.4).sp,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "뒤로",
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::refresh, enabled = !state.isRefreshing) {
                        Icon(
                            Icons.Outlined.Refresh,
                            contentDescription = "새로고침",
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    IconButton(onClick = viewModel::delete) {
                        Icon(
                            Icons.Outlined.Delete,
                            contentDescription = "삭제",
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        when {
            state.isLoading -> LoadingBox(padding)
            state.detail != null -> DetailContent(detail = state.detail!!, padding = padding)
            else -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(
                    text = "정보를 불러올 수 없어요.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun LoadingBox(padding: PaddingValues) {
    Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
internal fun DetailContent(detail: TrackingDetail, padding: PaddingValues) {
    LazyColumn(
        contentPadding = PaddingValues(
            top = padding.calculateTopPadding() + 6.dp,
            bottom = padding.calculateBottomPadding() + 28.dp,
            start = 16.dp,
            end = 16.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        item { SummaryHeader(tracking = detail.summary) }
        item {
            Column {
                Text(
                    text = "배송 진행",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 4.dp, bottom = 14.dp),
                )
                if (detail.progresses.isEmpty()) {
                    Text(
                        text = "진행 내역이 없습니다.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 4.dp),
                    )
                } else {
                    Column {
                        detail.progresses.forEachIndexed { idx, p ->
                            TimelineRow(
                                progress = p,
                                isFirst = idx == 0,
                                isLast = idx == detail.progresses.lastIndex,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun SummaryHeader(tracking: Tracking) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        tonalElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    text = tracking.carrierId.displayName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.2).sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
                StatePill(state = tracking.state)
            }
            tracking.alias?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Text(
                text = tracking.trackingNumber,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 0.4.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = "최근 갱신 · ${tracking.updatedAt.formatKrFull()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.78f),
                modifier = Modifier.padding(top = 2.dp),
            )
        }
    }
}

@Composable
internal fun TimelineRow(
    progress: TrackingProgress,
    isFirst: Boolean,
    isLast: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
    ) {
        TimelineRail(
            state = progress.state,
            highlighted = isFirst,
            hasTail = !isLast,
        )
        Spacer(Modifier.width(14.dp))
        TimelineBody(
            progress = progress,
            isFirst = isFirst,
            isLast = isLast,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun TimelineRail(
    state: TrackingState,
    highlighted: Boolean,
    hasTail: Boolean,
) {
    val palette = state.palette()
    val bgColor = MaterialTheme.colorScheme.background
    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val ringColor = palette.background.copy(alpha = 0.22f)
    val dotColor = palette.background

    Column(
        modifier = Modifier
            .width(24.dp)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(3.dp))
        Canvas(modifier = Modifier.size(22.dp)) {
            if (highlighted) {
                drawCircle(color = ringColor, radius = 11.dp.toPx())
            } else {
                drawCircle(color = outlineColor, radius = 8.dp.toPx())
            }
            drawCircle(color = bgColor, radius = 7.dp.toPx())
            drawCircle(color = dotColor, radius = 4.dp.toPx())
        }
        if (hasTail) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.outlineVariant),
            )
        }
    }
}

@Composable
private fun TimelineBody(
    progress: TrackingProgress,
    isFirst: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(bottom = if (isLast) 4.dp else 22.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = progress.state.label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isFirst) FontWeight.Bold else FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = progress.time.formatKrShort(),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFeatureSettings = "tnum",
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
            )
        }
        val detailLine = buildString {
            val loc = progress.location?.takeIf { it.isNotBlank() }
            val desc = progress.description.takeIf { it.isNotBlank() }
            when {
                loc != null && desc != null -> append("$loc · $desc")
                loc != null -> append(loc)
                desc != null -> append(desc)
            }
        }
        if (detailLine.isNotEmpty()) {
            Spacer(Modifier.height(3.dp))
            Text(
                text = detailLine,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isFirst) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
