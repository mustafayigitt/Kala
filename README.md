# Kala: Android Resource Management Library
[![mustafayigitt - kala](https://img.shields.io/static/v1?label=mustafayigitt&message=kala&color=blue&logo=github)](https://github.com/mustafayigitt/kala "Go to GitHub repo")

[![stars - kala](https://img.shields.io/github/stars/mustafayigitt/kala?style=social)](https://github.com/mustafayigitt/kala)
[![forks - kala](https://img.shields.io/github/forks/mustafayigitt/kala?style=social)](https://github.com/mustafayigitt/kala)
[![issues - kala](https://img.shields.io/github/issues/mustafayigitt/kala)](https://github.com/mustafayigitt/kala/issues)

[![GitHub release](https://img.shields.io/github/release/mustafayigitt/kala?include_prereleases=&sort=semver&color=blue)](https://github.com/mustafayigitt/kala/releases/)
[![License](https://img.shields.io/badge/License-Apache-blue)](#license)

Kala is an Android resource management library that allows you to easily manage your strings, drawables, and other resources. With Kala, you can centralize your resource management in one place, manage multiple language translations, and easily retrieve and manipulate resources from your application code.

## Features
  - Centralized resource management
  - Support for multiple language translations
  - Outsource resource management
  - Retrieval of string and drawable resources with type-safety
  - Automatic caching for remote resource requests

## Dependencies
Add the Jitpack source to project:
```kotlin
  ...
  repositories {
    maven { url 'https://jitpack.io' } 
  }
```

Add the following dependency to your build.gradle(module) file:
```kotlin
  dependencies {
    ...
    implementation 'com.github.mustafayigitt:kala:1.0.0'
  }
```

## ResourceKey
ResourceKey class is a utility class that provides a type-safe and convenient way to access resources in an Android app. It is used to retrieve resources such as strings and drawables from the app's resource files.

ResourceKey is a generic class that takes a type parameter T which must be a non-null Any type. It also takes a key parameter of type String, which represents the name or identifier of the resource to retrieve.
```kotlin
class ResourceKey<T : Any>(
    private val type: KClass<T>,
    private val key: String
)
```
ResourceKey provides two operator functions to access resources. The first function is the invoke() function, which returns the resource object of type T. The second function is an overloaded invoke() function, which takes a variable number of arguments and returns the formatted string resource.
These operators are powered by **Kotlin Context-Receivers.**
The `getString` and `getDrawable` functions are accessed from the `IResourceManager` implemented in the `ResourceManager.`
```kotlin
    context(IResourceManager, Context)
    @Suppress("UNCHECKED_CAST")
    operator fun invoke(): T {
        return when (type) {
            String::class -> getString(key) as T
            Drawable::class -> getDrawable(this@Context, key) as T
            else -> throw IllegalArgumentException("Resource type is not supported")
        }
    }

    context(IResourceManager)
    operator fun invoke(vararg args: Any): String {
        return getString(key, *args)
    }
```
ResourceKey provides a companion object that has a reified invoke() function to create a new instance of the ResourceKey class with the specified type and key.
```kotlin
    companion object {
        inline operator fun <reified T : Any> invoke(key: String) = ResourceKey(T::class, key)
    }
    // Usage
    val HELLO = ResourceKey<String>("app.strings.hello")    
```


## Usage
### Define ResourceKeys
```kotlin
object ResourceKeys {

    // STRING KEYS
    val HELLO = ResourceKey<String>("app.strings.hello")
    val CHANGE_LANGUAGE = ResourceKey<String>("app.strings.change_language")
    val SUCCESS = ResourceKey<String>("app.strings.success")

    // OTHER KEYS
    val TEXT_FROM_OUTSOURCE = ResourceKey<String>("app.strings.text_from_outsource")

    // DRAWABLE KEYS
    val COUNTRY_FLAG = ResourceKey<Drawable>("flag")

}
```

### Init
First, you need to initialize the ResourceManager singleton instance. The ResourceManager is responsible for managing all resources, caching remote resources, and serving resources to your application code.
```kotlin
  // Initialize the ResourceManager
  ResourceManager.setLanguage("en")
  ResourceManager.provideStrings(getEnglishStrings())
```
### String Resources
To retrieve a string resource, use the ResourceKey<String> class. You can define your own string resources in the ResourceKeys object.
```kotlin
  with(ResourceManager){ // Need to IResourceManager as context (Context-Receivers) 
    val helloString = ResourceKeys.HELLO() // Returns the string resource with key "app.strings.hello"
  }
```
You can also retrieve a formatted string resource:
```kotlin
  with(ResourceManager){ // Need to IResourceManager as context (Context-Receivers)
    val successString = ResourceKeys.SUCCESS("John Doe") // Returns "Success, John Doe!"
  }
```
### Drawable Resources
To retrieve a drawable resource, use the ResourceKey<Drawable> class. You can define your own drawable resources in the ResourceKeys object.
```kotlin
  with(ResourceManager){ // Need to IResourceManager as context (Context-Receivers)
    val flagDrawable = ResourceKeys.COUNTRY_FLAG() // Returns the drawable resource with key "flag"
  }
```
### Use without ResourceManager scope
```kotlin
  txtLabel.text = ResourceManager.getString("${ResourceKeys.HELLO}")
```

### Outsource Resource Management (Remote/Local/Other)
Kala also supports outsourcing resource management. To use an outsourced resource provider, set the outsourceStringProvider property of the ResourceManager singleton instance.
```kotlin
  // Set the outsourced resource provider
  ResourceManager.outsourceStringProvider = ::getStringFromCache

  private fun getStringFromCache(lang: String, key: String): String? {
     return when (lang) {
         "en" -> when (key) {
             "${ResourceKeys.TEXT_FROM_OUTSOURCE}" -> "This text is from cache."
             else -> null
         }
         "tr" -> when (key) {
             "${ResourceKeys.TEXT_FROM_OUTSOURCE}" -> "Bu metin cache'ten alındı."
             else -> null
         }
         else -> null
     }
  } 


```
### Clearing Cache
You can clear the resource cache by calling the clear function on the ResourceManager singleton instance.
```kotlin
  // Clear the resource cache
  ResourceManager.clear()
```

License
=======
    Copyright 2023 @mustafayigitt

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
