package com.link_up.matching_manager.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.MapKey
import dagger.Module
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

/*
 * Dagger2를 사용하여 Android Architecture Components의
 * ViewModel을 인스턴스화하는 데 사용되는 ViewModelProvider.Factory구현
 * ViewModel의 의존성을 주입할 수 있다.
 *
 * @ViewModelKey Annotation : ViewModel 클래스를 식별하기 위한 Dagger Map의 키로 사용됨
 *
 * create()메서드 : ViewModelProvider.Factory의 메서드로, 특정 클래스의 ViewModel을 생성
 *
 * 의존성 주입을 사용하여 ViewModel이 필요한 모든 곳에서 일관된 방식으로 생성되고 사용됨
 *
 */

@MapKey
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class DaggerViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}

@Suppress("UNCHECKED_CAST")
class DaggerViewModelFactory @Inject constructor(
    private val creators: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val found = creators.entries.find {
            modelClass.isAssignableFrom(it.key)
        }
        val creator = found?.value ?: throw IllegalArgumentException("unknown class $modelClass")

        try {
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}