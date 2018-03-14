package server.springcontext.testmodules

import server.springcontext.datadefinition.DataDefinition
import server.springcontext.datadefinition.ModelDefinition

interface Module {
    fun getDataDefinition():DataDefinition
    fun getModelDefinition(): ModelDefinition
    fun unload()
}