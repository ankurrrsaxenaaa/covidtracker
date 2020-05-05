# COVID Tracker 0.1.0

### How to run the application

I have included a **dockerfile** inside the project root directory. You can run this project as a docker image. You need to have docker installed on the system if you wish to do so.

The steps to run the docker image in a container are as follows:

1. Clone this project into a directory in your system.

2. Open the terminal and run a clean maven build. This will build a jar file in the target folder.

3. Switch to root user to use dokcerfile and go into the project directory.

4. Run the command ```docker login``` and put in your dockerhub username and password. This should authorise you.

5. Run the command ```docker build -f Dockerfile -t covidtracker .``` 

   This will build a docker image for you.

6. Run the command ```docker images``` to check if the image has been created. You should see an image with the name covidtracker and an openjdk image.

7. Run the command ```docker run -p 8080:8080 -d covidtracker``` 

   This will run the image into a container in detached mode.

8. Run the command ```docker ps -a``` to see the list of containers. You should see the container with the image named covid tracker.

9. Now you can access all the APIs of the project on your system's localhost at port 8080.

10. All the APIs have the url ```/api/covid/records``` 

### Story 1

Add an API to upload initial data in CSV format. The CSV will specify a location with the number of people in the following different stages of COVID. The stages of COVID are: 

- Tested
- Confirmed 
- Active 
- Recovered 
- Dead

On uploading the data the application must replace all previous data for the specified cities. 

Request :

```
Content-Type: text/csv 

Id,Location,Tested,Confirmed,Active,Recovered,Dead

1,Delhi,3000,51,49,1,1 

2,Gujrat,11000,545,544,0,1 

3,Kerala,7000,145,144,1,0
```

Response : ``201`` with a JSON message.

*In case the file format is incorrect then following response should be returned with HTTP status code ```400```*

```json
{ "msg" : "File format not supported. Please use specified CSV format" }
```

### Story 2 : Update City Data

Add an API to update data for a specific location. The request will contain number of people in different stages of COVID. All non specified stages can be assumed to be 0.

**The application must hold data in an incremental manner. It will be used to determine analytics (growth, comparative analysis) across different stages of COVID**

The request body is shown below.

```json
{
 "Tested":150,
 "Confirmed":10,
 "Active":1 
}
```

Response : ```200``` with a JSON message having the aggregated data for a location.

```json
 {
  "Tested":3150, 
  "Confirmed":61, 
  "Active":50, 
  "Recovered":1,
  "Dead":1 
}
```

*The API must validate an existing Location in case-insensitive manner. An invocation with non-existing Location should return error in a valid JSON response message.*

### Story 3 : Search

The application should provide a search API which can handle the following filters :

#### Location (optional)

Selects the locations which are used to select a values. It is possilbe to select a single location or multiple locations. e.g. ```location=Delhi``` ```location=Delhi&location=Gujrat``` The filter is **NOT** mandatory. In case no value is provided the application should search with all available locations.

#### Type (optional)

This filter can be used to perform computation on the different stages of the selected data.

- total : computes the total of the different stages for selected data e.g. ```type=total```

  - min : computes the minimum based on the selected column e.g.

    ```type=min&selected=Active```

  - max : computes the maximum based on the selected column e.g

    ```type=min&selected=Confirmed```

The ```type``` operation return back a JSON list with single value.

```json
[{
"Tested":3150,
"Confirmed":61,
"Active":50,
"Dead":1
}]
```

The filter is **NOT** mandatory. In case no value is provided the application should return all selected values.