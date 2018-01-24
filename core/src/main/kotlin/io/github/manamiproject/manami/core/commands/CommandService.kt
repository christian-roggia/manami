package io.github.manamiproject.manami.core.commands


internal interface CommandService {

    fun isUnsaved(): Boolean

    fun setUnsaved(value: Boolean)

    fun executeCommand(command: ReversibleCommand): Boolean

    fun undo()

    fun redo()

    fun clearAll()

    fun resetDirtyFlag() //FIXME: This should be internal part of setUnsaved()

    fun isEmptyDoneCommands(): Boolean

    fun isEmptyUndoneCommands(): Boolean
}