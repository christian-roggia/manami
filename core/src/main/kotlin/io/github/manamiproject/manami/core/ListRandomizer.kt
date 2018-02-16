package io.github.manamiproject.manami.core

import java.security.SecureRandom

object ListRandomizer {

    fun randomizeOrder(list: MutableList<out Any>) {
        list.shuffle(SecureRandom())
        list.shuffle(SecureRandom())
        list.shuffle(SecureRandom())
        list.shuffle(SecureRandom())
    }
}