package no.ordbokene

class Greeting {
  private val platform: Platform = getPlatform()

  fun greet() = "Hello and welcome to Ordbøkene - ${platform.name}!"
}
