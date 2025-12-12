package com.muhammad.lumina.data

import com.muhammad.lumina.presentation.screens.edit_photo.EditAction

class EditHistoryManager {
    private val undoStack = ArrayDeque<EditAction>()
    private val redoStack = ArrayDeque<EditAction>()

    fun push(action: EditAction){
        undoStack.addLast(action)
        redoStack.clear()
    }
    fun undo() : EditAction?{
        val action = undoStack.removeLastOrNull() ?: return null
        redoStack.addLast(action)
        return action
    }
    fun redo() : EditAction?{
        val action = redoStack.removeLastOrNull() ?: return null
        undoStack.addLast(action)
        return action
    }
    fun canUndo() = undoStack.isNotEmpty()
    fun canRedo() = redoStack.isNotEmpty()
    fun clear(){
        undoStack.clear()
        redoStack.clear()
    }
}