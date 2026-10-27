package com.a.server.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan(value = ["com.a.server"])
class AndroidModule {
}