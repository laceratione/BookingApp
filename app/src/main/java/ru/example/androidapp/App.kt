package ru.example.androidapp

import android.app.Application
import ru.example.androidapp.di.AppComponent
import ru.example.androidapp.di.DaggerAppComponent

class App: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().build()
    }
}