# FlickrImageViewer
An Android App which provides search functionality for images using [Flickr APIs](https://www.flickr.com/services/api/)

## Key Feature
  - Search functionality on images and a 3-Column-Grid-layout (RecyclerView) to display the result 
  - Supports both *Landscape* and *Portrait* layout (Handles rotatation/configuration changes)
  - MVVM Architecture with LiveData, ViewModel, etc
  - Pagination added using an Infinite Scroll Mechanism
  - Image data caching with Glide
  - TestCases Covered
  
  
## Third Party Library
  - Glide: To load imageUrl and cache the data
  - Gson: For data parsing
  - Retrofit: For network calls
  - Android JetPack: For LiveData and ViewModel
  - Android Core Libraries: CardView, RecyclerView, Legacy-Support
  - Kotlin Coroutine
  - Facebook's Stetho for Android Debug Bridge
  - Espresso and JUnit for testing
