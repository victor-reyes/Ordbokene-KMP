package no.ordbokene

import android.app.Application
import no.ordbokene.di.appModule
import no.ordbokene.di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class Application : Application() {

  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidLogger()
      androidContext(this@Application)
      modules(appModule, sharedModule)
    }
  }
}
