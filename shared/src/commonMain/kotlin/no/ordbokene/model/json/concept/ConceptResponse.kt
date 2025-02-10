package no.ordbokene.model.json.concept

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class ConceptResponse(val id: String, val name: String, val concepts: Map<String, Concept>)

@Serializable data class Concept(@SerialName("class") val clazz: String? = null, val expansion: String)
