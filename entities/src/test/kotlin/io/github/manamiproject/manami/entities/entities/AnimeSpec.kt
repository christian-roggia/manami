package io.github.manamiproject.manami.entities.entities

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class AnimeSpec : Spek({

    given("a valid anime created with mandatory parameters only") {
        var anime = Anime("Death Note", InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535"))

        beforeEachTest {
            anime = Anime("Death Note", InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535"))
        }

        on("setting the amount of episodes to an invalid value") {
            anime.episodes = -1

            it("must use the default value 0") {
                assertThat(anime.episodes).isZero()
            }
        }

        on("checking if the anime is valid") {
            val result = anime.isValid()

            it("must return true, because the default parameter are using valid values") {
                assertThat(result).isTrue()
            }
        }
    }


    given("an anime with an empty title") {
        val anime = Anime("", InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535"))

        on("checking if the anime is valid") {
            val result = anime.isValid()

            it("must return false, because a title cannot be empty") {
                assertThat(result).isFalse()
            }
        }
    }


    given("an anime with a blank title") {
        val anime = Anime("     ", InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535"))

        on("checking if the anime is valid") {
            val result = anime.isValid()

            it("must return false, because a title cannot solely consist of whitespaces") {
                assertThat(result).isFalse()
            }
        }
    }


    given("an anime with an empty location") {
        val anime = Anime("Death Note", InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535"))
        anime.location = ""

        on("checking if the anime is valid") {
            val result = anime.isValid()

            it("must return false, because a location cannot be empty") {
                assertThat(result).isFalse()
            }
        }
    }


    given("an anime with a blank location") {
        val anime = Anime("Death Note", InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535"))
        anime.location = "   "

        on("checking if the anime is valid") {
            val result = anime.isValid()

            it("must return false, because a location cannot solely consist of whitespaces") {
                assertThat(result).isFalse()
            }
        }
    }


    given("an valid anime with an amount of episodes > 0") {
        val anime = Anime("Death Note", InfoLink("${NORMALIZED_ANIME_DOMAIN.MAL.value}1535"), 4)

        on("setting the amount of episodes to an invalid value") {
            anime.episodes = -1

            it("must not change the number of episodes") {
                assertThat(anime.episodes).isEqualTo(4)
            }
        }

        on("setting the amount of episodes to an valid value") {
            anime.episodes *= 2

            it("must not change the number of episodes") {
                assertThat(anime.episodes).isEqualTo(8)
            }
        }
    }
})