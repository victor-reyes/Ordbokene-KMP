package no.ordbokene.model.json.lookup.ids

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class RelationId {
  @SerialName("el") EL,
  @SerialName("jf") JF,
  @SerialName("t_forsk_f") T_FORSK_F,
  @SerialName("t_forskj_fra") T_FORSK_FRA,
  @SerialName("mots") MOTS,
  @SerialName("bet") BET,
  @SerialName("sms_er") SMS_ER,
  @SerialName("forb_r") FORB_R,
  @SerialName("i_forb") I_FORB,
  @SerialName("opphl") OPPHL,
  @SerialName("i_forb_m") I_FORB_M,
  @SerialName("forb") FORB,
  @SerialName("sms") SMS,
  @SerialName("opph") OPPH,
  @SerialName("t_skiln") T_SKILN,
  @SerialName("sm_o_s") SM_O_S,
  @SerialName("besl") BESL,
  @SerialName("gj") GJ,
  @SerialName("smbl") SMBL,
  @SerialName("smh") SMH,
  @SerialName("sm_o") SM_O,
  @SerialName("avl") AVL,
}
