# Public Media hub for images

#### This sample was created to showcase my skills and the latest learning in the Android Framework.

**Instructions: In the gradle.properties file there is the BASE_API_URL where it includes an IP and the port that the server is running on.
**This probably will need to be modified according to the server

Some technologies:

Used Glide for efficient image loading

Used Navigation component to provide the navigation in the app

Kotlin-coroutines were used for blocking operations(fetching images list from the server,
compressing and uploading images...)

Used LiveData and ViewModels as well

Koin was used to glue the different components together in the application

Included unit tests as well. The tests mainly make sure that the view model is emitting the right
state after each action


### Architecture:

I am using the MVVM architecture and some state machine concept on top of it.
Every screen has a view, a model, and a ViewModel. The ViewModel contains a state that represents
the properties of the View. This state will be emitted using LiveData to the observer(view).

The ViewModel state is represented using a simple kotlin data class with different fields.

I also use sealed classes to model some repetitive behaviors. Like, when fetching data in an
asynchronous fashion, the usual states are Loading, Failed(with the failure), or Success(with the
actual data).

Repository is the single source of truth that is used to fetch data(either from the network or
from the local storage)

### Future Enhancements:

Images in the list can be clickable that navigates to a full image screen with the ability to swipe
left and right(using viewpager)
Some UI enhancements and effects


