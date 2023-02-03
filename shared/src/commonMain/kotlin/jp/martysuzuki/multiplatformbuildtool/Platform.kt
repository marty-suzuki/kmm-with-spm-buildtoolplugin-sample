package jp.martysuzuki.multiplatformbuildtool

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform