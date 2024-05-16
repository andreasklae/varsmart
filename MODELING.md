# MODELING

## Functional requirements
In this section the most important functional requirements from our requirement specification <br>
are represented with four use-cases, four sequence diagrams and one flowchart<br>

Important functions in our app:
- Check the weather for a location
- Get a weather-related tip from mr. practical
- Add a city to favorite list
- Check alerts nearby you

## USE-CASES

Use-case 1: Check the weather for a location <br>
Actor: User <br>
Pre-condition: User has internet-access <br>
Post-condition: Weather-data is displayed on the Home-screen <br>

- Description:
1. The user opens the app and is on the home-screen 
2. The user navigates to the search-screen
3. The user clicks on the search-tool
4. The user writes the city-name in the searchbar
5. The user clicks on the city option.
6. The system provides weather-data for the city on the home-screen

See Diagrams/sequenceDiagram1CheckWeatherForLocation.md for the sequence diagram


-----------------------

Use-case 2: Add an optional city in Norway as a favorite. <br>
Actor: User <br>
Pre-condition: User has internet-access <br>
Post-condition: The city is added to the favorite-list overview <br>

Description:

1. The user opens the app and is on the home-screen
2. The user navigates to the search-screen
3. The user clicks on the search-tool
4. The user writes the city-name in the searchbar
5. The system displays the city as an option on the screen.
6. The user clicks on a bookmark-icon to add the city as a favorite

Alternative flow: Checks weather-data first <br>

5.1 The user clicks on the city option <br>
5.2 The system provides weather-data for the city on the home-screen <br>

See Diagrams/sequenceDiagram2AddFavourite.md for the sequence diagram

----------------------


Use-case 3: Get a weather-related tip from mr. practical <br>
Actor: User <br>
Pre-condition: Weather-data for a location is displayed on the Home-screen  <br>
Post-condition: Mr.Practical gives a weather-related tip <br>

- Description:
1. The user clicks on Mr.Practical for a tip
2. Mr.practical generates and displays a message on the screen

See Diagrams/sequenceDiagram3Mr.PraktisTips.md for the sequence diagram



---------------------
Use-case 4: Check alerts nearby you <br>
Actor: User <br>
Pre-condition: User has internet-access and is on the home-screen w/a location <br>
Post-condition: The user gets alerts or message about no alerts nearby <br>

- Description:
1. The home-screen displays a dropdown-box with the nearest alert(s), or a msg about no alert.

See Diagrams/sequenceDiagram4AlertsNearBy.md for the sequence diagram

### USE CASE- DIAGRAM
-->These use cases are represented in a use-case-diagram: <br>
![img_1.png](usecaseDiagram.png)



### Flowchart

Activity: User opens the app for the first time and goes through onboarding


See Diagrams/OpenAppFlowChart.md for the flowchart

### Class diagram

See Diaframs/ClassDiagram.svg

