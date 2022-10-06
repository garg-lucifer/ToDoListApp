<h1>ToDoListApp</h1>

<p>
  <a href="https://opensource.org/licenses/MIT"><img alt="License" src="https://img.shields.io/badge/License-MIT-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=26"><img alt="API" src="https://img.shields.io/badge/API-26%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://proandroiddev.com/everything-you-need-to-know-about-adding-notifications-with-alarm-manager-in-android-cb94a92b3235"><img alt="Medium" src="https://skydoves.github.io/badges/Story-Medium.svg"/></a>
</p>

<p >  
ToDoList App demonstrates modern Android development with Hilt, Coroutines, LiveData, Jetpack (Room, ViewModel), and Material 3 Design based on MVVM architecture. 
</p>

# Screenshots:
<img src="/screenshots/day1.png" height="400px"/> <img src="/screenshots/night1.png" height="400px"/> 
<img src="/screenshots/day2.png" height="400px"/> <img src="/screenshots/night2.png" height="400px"/> 
<img src="/screenshots/day3.png" height="400px"/> <img src="/screenshots/night3.png" height="400px"/>
<img src="/screenshots/day4.png" height="400px"/> <img src="/screenshots/night4.png" height="400px"/> 

## Tech stack & Open-source libraries
- Minimum SDK level 26
- [Kotlin](https://kotlinlang.org/)
  - Lifecycle: Observe Android lifecycles and handle UI states upon the lifecycle changes.
  - ViewModel: Manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
  - DataBinding: Binds UI components in your layouts to data sources in your app using a declarative format rather than programmatically.
  - Room: Constructs Database by providing an abstraction layer over SQLite to allow fluent database access.
  - Navigation Component Graphs - To use single activity multiple fragment architecture.
  - [Hilt](https://dagger.dev/hilt/): For dependency injection.
  - [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) For asynchronous programming.
- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model)
  - Repository Pattern
- [Material-Components](https://github.com/material-components/material-components-android): Material design components for building ripple animation, and CardView.
- [ColorPicker](https://github.com/QuadFlask/colorpicker): Simple android color picker with color wheel and lightness bar.

## Architecture
**ToDoListApp** is based on the MVVM architecture and the Repository pattern, which follows the [Google's official architecture guidance](https://developer.android.com/topic/architecture).

![architecture](figure/figure0.png)

## Room Database Structure
![database structure](figure/RoomDBStructure.png)

# License:
MIT License

Copyright (c) 2022 Naman Garg

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
