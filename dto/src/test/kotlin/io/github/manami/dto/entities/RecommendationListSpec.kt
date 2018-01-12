package io.github.manami.dto.entities

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class RecommendationListSpec : Spek({

    given("a newly created RecommendationList") {
        var recommendationList = RecommendationList()

        beforeEachTest {
            recommendationList = RecommendationList()
        }

        on("checking if the list is empty") {
            val isEmpty = recommendationList.isEmpty()
            val isNotEmpty = recommendationList.isNotEmpty()

            it("must return true for isEmpty") {
                assertThat(isEmpty).isTrue()
            }

            it("must return false for isNotEmpty") {
                assertThat(isNotEmpty).isFalse()
            }

            it("must have a size of 0") {
                assertThat(recommendationList.size).isZero()
            }
        }

        on("adding a new recommendation") {
            val infoLink = InfoLink("http://myanimelist.net/anime/1535")
            val recommendation = Recommendation(infoLink, 103)

            recommendationList.addRecommendation(recommendation)

            it("must return false for isEmpty") {
                assertThat(recommendationList.isEmpty()).isFalse()
            }

            it("must return true for isNotEmpty") {
                assertThat(recommendationList.isNotEmpty()).isTrue()
            }

            it("must contain the inserted infoLink") {
                assertThat(recommendationList.contains(infoLink)).isTrue()
            }

            it("must be returned upon get() call") {
                assertThat(recommendationList.get(infoLink)).isEqualTo(recommendation)
            }
        }

        on("checking if the list contains a Recommendation") {
            val result = recommendationList.contains(
                    Recommendation(
                            InfoLink("http://myanimelist.net/anime/1535"),
                            231
                    )
            )

            it("must return false") {
                assertThat(result).isFalse()
            }
        }

        on("retrieving iterator") {
            val result = recommendationList.iterator()

            it("must not have any new items") {
                assertThat(result.hasNext()).isFalse()
            }
        }
    }

    given("a recommendation list containing one recommendation") {
        val infoLinkUrl = "http://myanimelist.net/anime/1535"
        val initialValue = 5
        var recommendationList = RecommendationList()


        beforeEachTest {
            recommendationList = RecommendationList().apply {
                addRecommendation(
                        Recommendation(
                                InfoLink(infoLinkUrl),
                                initialValue
                        )
                )
            }
        }


        on("adding a new instance of the same infoLinkUrl with a different amount") {
            val newValue = 15
            recommendationList.addRecommendation(
                    Recommendation(
                            InfoLink(infoLinkUrl),
                            newValue
                    )
            )

            it("must contain only one entry") {
                assertThat(recommendationList.size).isOne()
            }

            it("must contain the infoLink with the sum of both values as amount") {
                assertThat(recommendationList.get(InfoLink(infoLinkUrl))?.amount).isEqualTo(initialValue + newValue)
            }
        }

        on("checking if the list contains the Recommendation (different instance)") {
            val result = recommendationList.contains(
                    Recommendation(
                            InfoLink(infoLinkUrl),
                            initialValue
                    )
            )

            it("must return true") {
                assertThat(result).isTrue()
            }
        }

        on("checking if the list contains the recommendations (different instance) using containsAll") {
            val result = recommendationList.containsAll(
                    mutableListOf(
                            Recommendation(
                                    InfoLink(infoLinkUrl),
                                    initialValue
                            )
                    )
            )

            it("must return true") {
                assertThat(result).isTrue()
            }
        }

        on("checking if the list containsAll of recommendations which reside in the list and some which don't") {
            val result = recommendationList.containsAll(
                    mutableListOf(
                            Recommendation(
                                    InfoLink(infoLinkUrl),
                                    initialValue
                            ),
                            Recommendation(
                                    InfoLink("http://myanimelist.net/anime/35180"),
                                    5
                            )
                    )
            )

            it("must return false") {
                assertThat(result).isFalse()
            }
        }

        on("retrieving iterator") {
            val result = recommendationList.iterator()

            it("must not have new items") {
                assertThat(result.hasNext()).isTrue()
            }
        }
    }
})