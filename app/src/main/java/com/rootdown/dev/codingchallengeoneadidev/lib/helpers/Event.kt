package com.rootdown.dev.codingchallengeoneadidev.lib.helpers

open class Event<out T>(private val content: T){

    var handled = false
        private set

    /*
    * returns the generic in and prevents re-instantiation
     */
    fun getIfNothandled(): T?{
        return if (handled){
            null
        } else {
            handled = true
            content
        }
    }

    /*
    * Return the content, even if it has been handled
    * */
    fun getContent(): T = content
}