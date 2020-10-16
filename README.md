## Firestore-Example-Project

A simple example project to demo Firebase setup integrating Cloud Firestore.

### Description

In this project, a user can add a title and description to a note and add it to the recyclerview. The note will be saved in Firestore DB and the user will be alerted with a Toast message of the status. 

All notes are automatically requested unordered when first fetched in the `onStart()` method, but a user can manually request all the notes by clicking the `Load by date added` button - which will also order the notes by date added (descending order). This is to demonstrate a simple sort filter.

A user can click on a note to reveal an alert dialog with further options, such as; updating a description, deleting a description, and deleting a note in the list.

### Setup

In order for you to use this project, you will need to follow the necessary steps to setup Firebase. The project itself is already configured for you. I found [this](https://www.youtube.com/watch?v=dRYnm_k3w1w) video useful when setting up Firebase. You will need to do this in order to generate your `google-services.json` file. Once you have done this and added it to the root of the `app` module of the project, you will just need to create the Cloud Firestore database, which is only a couple of clicks and done inside of Firebase. Once this is done, you should be good to go! 

Firestore collection name: `Notebook`

Main components include; Clean Architecture with MVVM repository pattern, LiveData, navigation graph, RXJava, RecyclerView, Unit and Instrument tests.



