import kotlinx.serialization.json.Json
import no.ordbokene.model.ArticleResponse
import kotlin.test.Test

class ArticleResponseTest {

  companion object {
    val sampleData =
      listOf(
        """{"article_id": 54131, "submitted": "2024-01-11 11:52:11", "suggest": ["skår", "score"], "lemmas": [{"added_norm": false, "final_lexeme": "skår", "hgno": 3, "id": 62373, "inflection_class": "m1", "lemma": "skår", "paradigm_info": [{"from": "1996-01-01", "inflection": [{"tags": ["Sing", "Ind"], "word_form": "skår"}, {"tags": ["Sing", "Def"], "word_form": "skåren"}, {"tags": ["Plur", "Ind"], "word_form": "skårer"}, {"tags": ["Plur", "Def"], "word_form": "skårene"}], "inflection_group": "NOUN_regular", "paradigm_id": 564, "standardisation": "STANDARD", "tags": ["NOUN", "Masc"], "to": null}], "split_inf": false}, {"added_norm": false, "final_lexeme": "score", "hgno": 1, "id": 57710, "inflection_class": "m1", "lemma": "score", "paradigm_info": [{"from": "1996-01-01", "inflection": [{"tags": ["Sing", "Ind"], "word_form": "score"}, {"tags": ["Sing", "Def"], "word_form": "scoren"}, {"tags": ["Plur", "Ind"], "word_form": "scorer"}, {"tags": ["Plur", "Def"], "word_form": "scorene"}], "inflection_group": "NOUN_regular", "paradigm_id": 566, "standardisation": "STANDARD", "tags": ["NOUN", "Masc"], "to": null}], "split_inf": false}], "body": {"pronunciation": [{"type_": "pronunciation", "content": "skår", "items": []}], "etymology": [{"type_": "etymology_reference", "content": "av ${'$'} ${'$'}, ‘hakk, innsnitt, poeng’", "items": [{"type_": "entity", "id": "eng."}, {"type_": "usage", "items": [], "text": "score"}]}], "definitions": [{"type_": "definition", "elements": [{"type_": "definition", "elements": [{"type_": "explanation", "content": "mål- eller poengstilling", "items": []}, {"type_": "explanation", "content": "resultat i en konkurranse", "items": []}, {"type_": "example", "quote": {"content": "en god ${'$'}", "items": [{"type_": "usage", "items": [], "text": "skår"}]}, "explanation": {"content": "", "items": []}}, {"type_": "example", "quote": {"content": "laget vant med en ${'$'} på 3–2", "items": [{"type_": "usage", "items": [], "text": "skår"}]}, "explanation": {"content": "", "items": []}}], "id": 2, "sub_definition": false}, {"type_": "definition", "elements": [{"type_": "explanation", "content": "resultat som en person oppnår i en prøve, en test ${'$'}", "items": [{"type_": "entity", "id": "e_l"}]}, {"type_": "example", "quote": {"content": "oppnå en høy ${'$'} i en test", "items": [{"type_": "usage", "items": [], "text": "skår"}]}, "explanation": {"content": "", "items": []}}, {"type_": "example", "quote": {"content": "hun fikk den høyeste skåren på skolen", "items": []}, "explanation": {"content": "", "items": []}}], "id": 4, "sub_definition": false}], "id": 3, "sub_definition": false}]}, "to_index": ["score", "skår", "skår", "skår"], "author": "27", "edit_state": "Eksisterende", "referers": [], "status": 8, "updated": "2024-12-13 09:55:30"}"""
      )

    private val format = Json { prettyPrint = true }
  }

  @Test
  fun shouldParseArticleJson() {
    val article = sampleData.first()
    val parsed = format.decodeFromString<ArticleResponse>(article)
    println(parsed)
  }
}
