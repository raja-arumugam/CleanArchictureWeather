package com.example.weather.di.component.app

import android.app.Application
import com.example.weather.App
import com.example.weather.di.modules.ActivityModule
import com.example.weather.di.modules.NetworkModule
import com.example.weather.di.modules.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidInjectionModule::class,
        NetworkModule::class,
        ActivityModule::class,
        ViewModelModule::class]
)
interface AppComponent {

    fun inject(application: App)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}