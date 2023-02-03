package jp.martysuzuki.multiplatformbuildtool

class Greeting {
    private val platform: Platform = getPlatform()

    fun greeting(): String {
        return "Hello World!\nThis is ${platform.name}!"
    }
}