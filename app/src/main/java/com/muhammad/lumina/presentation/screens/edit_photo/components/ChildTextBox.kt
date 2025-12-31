package com.muhammad.lumina.presentation.screens.edit_photo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammad.lumina.R
import com.muhammad.lumina.domain.model.Child
import com.muhammad.lumina.presentation.screens.edit_photo.ChildInteractionState
import com.muhammad.lumina.utils.rememberFillTextStyle
import com.muhammad.lumina.utils.rememberStrokeTextStyle
import kotlinx.coroutines.delay

@Composable
fun ChildTextBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp),
    child: Child,
    maxWidth: Dp,
    maxHeight: Dp,
    childInteractionState: ChildInteractionState,
    onChildClick: () -> Unit,
    onChildTextChange: (text: String) -> Unit,
    onChildDoubleClick: () -> Unit,
    onChildDeleteClick: (String) -> Unit,
) {
    val focusRequestor = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(childInteractionState) {
        if (childInteractionState is ChildInteractionState.Editing) {
            focusRequestor.requestFocus()
            delay(100)
            keyboardController?.show()
        }
    }
    LaunchedEffect(childInteractionState, child.id) {
        if (childInteractionState !is ChildInteractionState.Selected) {
            focusManager.clearFocus()
        }
    }
    Box(modifier = modifier) {
        val isChildSelected = when {
            child.isEmoji -> childInteractionState is ChildInteractionState.Selected && childInteractionState.id == child.id
            else -> (childInteractionState is ChildInteractionState.Selected && childInteractionState.id == child.id) || (childInteractionState is ChildInteractionState.Editing && childInteractionState.id == child.id)
        }
        val borderColor = if (isChildSelected) Color.White else Color.Transparent
        val isTextInEdit = childInteractionState is ChildInteractionState.Editing && childInteractionState.id == child.id && !child.isEmoji
        val backgroundColor = if (isTextInEdit) Color.Black.copy(0.15f) else Color.Transparent
        Box(
            modifier = Modifier
                .pointerInput(Unit){
                    detectTapGestures(onDoubleTap = {
                        if(!child.isEmoji){
                            onChildDoubleClick()
                        }
                    }, onTap = {
                        onChildClick()
                    })
                }
                .sizeIn(
                    maxWidth = maxWidth, maxHeight = maxHeight
                )
                .border(width = 2.dp, color = borderColor, shape = shape)
                .background(
                    color = backgroundColor, shape = shape
                )
        ) {
            val fontSize = if (child.isEmoji) 40.sp else 36.sp
            val textPadding = 8.dp
            val fillTextStyle = rememberFillTextStyle(fontSize = fontSize)
            val strokeTextStyle = rememberStrokeTextStyle(fontSize = fontSize)
            if (isTextInEdit) {
                OutlinedBricolageTextField(
                    text = child.text,
                    onTextChange = { newValue ->
                        onChildTextChange(newValue)
                    }, modifier = Modifier
                        .focusRequester(focusRequestor)
                        .padding(textPadding),
                    strokeTextStyle = strokeTextStyle,
                    fillTextStyle = fillTextStyle,
                    maxWidth = maxWidth - (textPadding * 2),
                    maxHeight = maxHeight - (textPadding * 2)
                )
            } else {
                OutlinedBricolageText(
                    text = child.text,
                    fillTextStyle = fillTextStyle,
                    strokeTextStyle = strokeTextStyle,
                    modifier = Modifier.padding(textPadding)
                )
            }
        }
        if (isChildSelected) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 12.dp, y = (-12).dp)
                    .clip(CircleShape)
                    .background(Color(0xFFB3261E))
                    .clickable(onClick = {
                        onChildDeleteClick(child.id)
                    }), contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_cancel),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.White
                )
            }
        }
    }
}