package com.mgpark.trackbox.ui.add

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mgpark.trackbox.domain.model.CarrierId
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    onBack: () -> Unit,
    onAdded: () -> Unit,
    viewModel: AddViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.submittedId) {
        if (state.submittedId != null) onAdded()
    }
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
                        text = "배송 추가",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        AddForm(
            state = state,
            padding = padding,
            onCarrier = viewModel::selectCarrier,
            onTrackingNumber = viewModel::onTrackingNumberChange,
            onAlias = viewModel::onAliasChange,
            onSubmit = viewModel::submit,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddForm(
    state: AddUiState,
    padding: PaddingValues,
    onCarrier: (CarrierId) -> Unit,
    onTrackingNumber: (String) -> Unit,
    onAlias: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    var sheetVisible by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = androidx.compose.runtime.rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 20.dp, end = 20.dp, top = 14.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            CarrierField(
                selected = state.carrier,
                expanded = sheetVisible,
                onClick = { sheetVisible = true },
            )

            FormField(
                label = "운송장 번호",
                helper = "숫자만 입력하면 자동으로 하이픈이 들어가요",
            ) { focused ->
                MonoTextField(
                    value = state.trackingNumber,
                    onChange = onTrackingNumber,
                    placeholder = "1234-5678-9012",
                    keyboardType = KeyboardType.Number,
                    visualTransformation = HyphenateTrackingNumber,
                    onFocusChanged = focused,
                )
            }

            FormField(
                label = "별칭",
                optional = true,
            ) { focused ->
                PlainTextField(
                    value = state.alias,
                    onChange = onAlias,
                    placeholder = "예: 엄마 선물",
                    onFocusChanged = focused,
                )
            }
        }

        SubmitButton(
            label = "추가하기",
            enabled = state.canSubmit,
            isLoading = state.isSubmitting,
            onClick = onSubmit,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 28.dp),
        )
    }

    if (sheetVisible) {
        CarrierBottomSheet(
            selected = state.carrier,
            sheetState = sheetState,
            onSelect = {
                onCarrier(it)
                scope.launch {
                    sheetState.hide()
                    sheetVisible = false
                }
            },
            onDismiss = { sheetVisible = false },
        )
    }
}

@Composable
private fun FormField(
    label: String,
    optional: Boolean = false,
    helper: String? = null,
    content: @Composable RowScope.(onFocusChanged: (Boolean) -> Unit) -> Unit,
) {
    var focused by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 2.dp),
            )
            if (optional) {
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "(선택)",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                )
            }
        }
        FieldShell(focused = focused) {
            content { focused = it }
        }
        if (helper != null) {
            Text(
                text = helper,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                modifier = Modifier.padding(start = 2.dp),
            )
        }
    }
}

@Composable
private fun FieldShell(
    focused: Boolean,
    onClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val borderColor by animateColorAsState(
        targetValue = if (focused) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.outline,
        label = "borderColor",
    )
    val borderWidth = if (focused) 1.5.dp else 1.dp

    val base = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .clip(RoundedCornerShape(14.dp))
        .background(MaterialTheme.colorScheme.surface)
        .border(borderWidth, borderColor, RoundedCornerShape(14.dp))

    val withClick = if (onClick != null) base.clickable(onClick = onClick) else base

    Row(
        modifier = withClick.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        content = content,
    )
}

@Composable
private fun CarrierField(
    selected: CarrierId?,
    expanded: Boolean,
    onClick: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
        Text(
            text = "택배사",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 2.dp),
        )
        FieldShell(focused = expanded, onClick = onClick) {
            Text(
                text = selected?.displayName ?: "택배사를 선택하세요",
                style = MaterialTheme.typography.bodyLarge,
                color = if (selected != null) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f),
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = if (expanded) Icons.Outlined.KeyboardArrowUp
                else Icons.Outlined.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun RowScope.MonoTextField(
    value: String,
    onChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
    visualTransformation: VisualTransformation,
    onFocusChanged: (Boolean) -> Unit,
) {
    val baseStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onSurface,
        fontFamily = FontFamily.Monospace,
        letterSpacing = 0.6.sp,
    )
    Box(modifier = Modifier.weight(1f)) {
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                style = baseStyle,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f),
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onChange,
            textStyle = baseStyle,
            singleLine = true,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = visualTransformation,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { onFocusChanged(it.isFocused) },
        )
    }
}

@Composable
private fun RowScope.PlainTextField(
    value: String,
    onChange: (String) -> Unit,
    placeholder: String,
    onFocusChanged: (Boolean) -> Unit,
) {
    val baseStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onSurface,
    )
    Box(modifier = Modifier.weight(1f)) {
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                style = baseStyle,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f),
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onChange,
            textStyle = baseStyle,
            singleLine = true,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { onFocusChanged(it.isFocused) },
        )
    }
}

@Composable
private fun SubmitButton(
    label: String,
    enabled: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.outlineVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (enabled) 6.dp else 0.dp,
            pressedElevation = if (enabled) 8.dp else 0.dp,
            disabledElevation = 0.dp,
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(20.dp),
            )
        } else {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CarrierBottomSheet(
    selected: CarrierId?,
    sheetState: androidx.compose.material3.SheetState,
    onSelect: (CarrierId) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            CarrierId.entries.forEach { carrier ->
                CarrierRow(
                    carrier = carrier,
                    selected = carrier == selected,
                    onClick = { onSelect(carrier) },
                )
            }
        }
    }
}

@Composable
private fun CarrierRow(carrier: CarrierId, selected: Boolean, onClick: () -> Unit) {
    val bg = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    else androidx.compose.ui.graphics.Color.Transparent
    val fg = if (selected) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.onSurface
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = carrier.displayName,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = fg,
            modifier = Modifier.weight(1f),
        )
        if (selected) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                tint = fg,
            )
        }
    }
}

private val HyphenateTrackingNumber = VisualTransformation { input ->
    val digits = input.text.filter(Char::isDigit)
    val formatted = buildString {
        digits.forEachIndexed { i, c ->
            if (i > 0 && i % 4 == 0) append('-')
            append(c)
        }
    }
    TransformedText(
        text = AnnotatedString(formatted),
        offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return 0
                val hyphensBefore = (offset - 1) / 4
                return (offset + hyphensBefore).coerceAtMost(formatted.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 0) return 0
                val safe = offset.coerceAtMost(formatted.length)
                val hyphensBefore = formatted.substring(0, safe).count { it == '-' }
                return safe - hyphensBefore
            }
        },
    )
}

