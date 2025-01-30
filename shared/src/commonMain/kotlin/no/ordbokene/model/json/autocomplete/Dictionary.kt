package no.ordbokene.model.json.autocomplete

enum class Dictionary {
  BM,
  NN,
  BOTH;

  companion object {
    fun fromCode(code: Int) =
      when (code) {
        1 -> BM
        2 -> NN
        3 -> BOTH
        else -> throw IllegalArgumentException("Invalid dictionary code: $code")
      }
  }
}
