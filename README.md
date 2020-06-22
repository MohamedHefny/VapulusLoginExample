# Vapulus Login Example
Login example using Vapulus api with MVVM and ViewState inspired.

## Libraries
* [Navigation components][1] - Build and structure your in-app UI, handle deep links, and navigate between screens.
* [LiveData][2] - Build data objects that notify views when the underlying database changes.
* [ViewModel][3] - Store UI-related data that isn't destroyed on app rotations.
* [Retrofit][4] -  Type-safe HTTP client networking library.

[1]: https://developer.android.com/topic/libraries/architecture/navigation
[2]: https://developer.android.com/topic/libraries/architecture/livedata
[3]: https://developer.android.com/topic/libraries/architecture/viewmodel
[4]: https://square.github.io/retrofit
   
## MVVM Architecture Pattern
The following diagram shows all the modules and how each module interact with one another after.
This architecture using a layered software architecture.
<p align="center">
  <img src="https://developer.android.com/topic/libraries/architecture/images/final-architecture.png"/>
</p>

   ## ViewState
   This example is using view state development approtch to expose and update the ui syncronized with the data layer.
