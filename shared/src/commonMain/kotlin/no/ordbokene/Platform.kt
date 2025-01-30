package no.ordbokene

interface Platform {
  val name: String
}

expect fun getPlatform(): Platform

expect fun initLogger()
