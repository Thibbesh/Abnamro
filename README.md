# Read me

## Architectural Choice

### Requirements.
1. Application must be production ready.
    * I have chosen Springboot.
2. REST application must be implemented using Java.
    * I have chosen Java 11
3. Data must be persisted in a database.
    * I have chosen postgres.
4. Use any frameworks of your choice for REST.
    * I have chosen spring-boot-starter-web
5. Unit testing must be taken in due consideration.
    * I have chosen spring-boot-starter-test, 
    * org.junit.platform,
6. Describe at least 10 testing scenarios using GivenWhenThen style.
    * Cucumber Test - Automate Gherkin Scenarios(find recipes.feature in test package)
7. The API's must be built ensuring that it is secured from security attacks.
    * spring-boot-starter-security
    * JWT Bearer access token based authentication
    * JWT Role based access to endpoints
    * all the request authenticated/protected by user signin.
    * CORS filter configuration
    * Disabled Cross Site Request Forgery (CSRF) OWASP foundation
    * Disabled x-frame-options

### Bonus

1. REST application should be secured by implementing authentication process (please provide credentials).
    * All endpoints are authenticated and Sign up and sign in
    * [SignUp](http://localhost:8080/api/authentication/signup)
    * [signin](http://localhost:8080/api/authentication/signin)
    * use accessToken to access recipe end points.
2. Application should have an API documentation.
    * I have chosen springdoc-openapi-ui
3. Write automation tests for the described testing scenarios.
    * No(Not implemented)
4. Use of container based solutions is an added advantage.
    * No(Not implemented)
5. Creating a single-page application illustrating the use of API.
    * No(Not implemented)    
    
### Technologies and frameworks.
* [Java 1.11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
* [Maven 3.6.1](https://maven.apache.org/download.cgi)
* [Spring-boot 2.5.2](https://spring.io/projects/spring-boot)
* [Spring-security](https://spring.io/projects/spring-security)
* [jjwt 0.9.1](https://jwt.io/introduction)
* [spring-boot-starter-data-jpa](https://spring.io/projects/spring-data-jpa)
* [Postgres database](https://www.postgresql.org/download/windows/)
* [springdoc-openapi-ui](https://springdoc.org/)
* [junit-platform-runner 1.6.0](https://junit.org/junit5/docs/current/user-guide/)    


### Run Recipes application
The following guides illustrate how to run recipes web application:
* Install Postgres database (https://www.postgresql.org/download/windows/)
* Create Database as Abnamro
    * CREATE DATABASE Abnamro;
* Checkout Code from (https://github.com/Thibbesh/Abnamro)
* run mvn clean install
* Run ./mvnw spring-boot:run
* Run insert query in Postgres database
    * INSERT INTO roles(name) VALUES('ROLE_USER');
    * INSERT INTO roles(name) VALUES('ROLE_MODERATOR');
    * INSERT INTO roles(name) VALUES('ROLE_ADMIN');
* Add postman chrome extension to chrome browser https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop?hl=en
* From postman/SOAP UI 
* ##### SignUp (http://localhost:8080/api/authentication/signup)
    * {
            "username": "Abc",
            "email": "Abc@xyz.com",
            "password": "psw123",
            "role": ["admin","user"]
       }   
 * ##### Signin http://localhost:8080/api/authentication/signin
    * {
            "username":"Abc",
            "password":"psw123"
       } 
 * To Access all recipes endpoints, in headers should pass  key as Authorization and value as  Bearer <accessToken from http://localhost:8080/api/authentication/signin>   
 * ##### POST End point (http://localhost:8080/api/recipe)
        * {
                  "name": "Kebbab",
                  "servings": 10,
                  "vegetarian": true,
                  "instructions": "1. Heat oven to 190C/170C fan/gas 5. Grease and flour two 20cm sandwich tins. 2. Place 200g softened unsalted butter, 200g caster sugar and 1 tsp vanilla extract into a bowl and beat well to a creamy consistency. 3. Slowly beat in 4 medium eggs, one by one, then fold in 200g self-raising flour and mix well. 4. Divide the mix between the cake tins, place into the oven and bake for about 20 mins until risen and golden brown. The cakes should spring back when gently pushed in the middle. 5. When ready, remove from the oven and allow to cool for 5 mins in the tin, before turning out onto a wire rack and cooling completely. 6. Spread about 6 tbsp raspberry jam onto one cake and top with 250ml whipped double cream. Sandwich the cakes together and dust with icing sugar.",
                  "ingredients": [
                  	{
                  		"name":"onion",
                  		"amount":"100"
                  		
                  	},
                  	{
                  		"name":"Tomoto",
                  		"amount":"400"
                  		
                  	},
                  	{
                  		"name":"wheat floor",
                  		"amount":"200 kg"
                  		
                  	},
                  	{
                  		"name":"mushroom",
                  		"amount":"100 kg"
                  		
                  	},
                  	{
                  		"name":"Chicken",
                  		"amount":"100 kg"
                  		
                  	}]
              }
* ##### GET End Point (http://localhost:8080/api/recipe/)
* ##### GET End Point with recipeId (http://localhost:8080/api/recipe/2)
* ##### PUT End Point (http://localhost:8080/api/recipe/2)
    * {
              "name": "Chicken Biriyani",
              "servings": 50,
              "created": "07‐07‐2021 23:38",
              "vegetarian": true,
              "instructions": "1. Heat oven to 190C/170C fan/gas 5. Grease and flour two 20cm sandwich tins. 2. Place 200g softened unsalted butter, 200g caster sugar and 1 tsp vanilla extract into a bowl and beat well to a creamy consistency. 3. Slowly beat in 4 medium eggs, one by one, then fold in 200g self-raising flour and mix well. 4. Divide the mix between the cake tins, place into the oven and bake for about 20 mins until risen and golden brown. The cakes should spring back when gently pushed in the middle. 5. When ready, remove from the oven and allow to cool for 5 mins in the tin, before turning out onto a wire rack and cooling completely. 6. Spread about 6 tbsp raspberry jam onto one cake and top with 250ml whipped double cream. Sandwich the cakes together and dust with icing sugar.",
              "ingredients": [
                  {
                      "name": "onion",
                      "amount": "10"
                  },
                  {
                      "name": "Tomoto",
                      "amount": "40"
                  },
                  {
                      "name": "wheat floor",
                      "amount": "20 kg"
                  },
                  {
                     "name": "mushroom",
                      "amount": "10 kg"
                  },
                  {
                      "name": "Chicken",
                      "amount": "10 kg"
                  }
              ]
      }
 * ##### DELETE End Point (http://localhost:8080/api/recipe/2)

* ### Postman API Test Collection
    * under /resources folder
    * Recipes Api.postman_collection.json
    
 * ### api-docs, swagger-ui
    * http://localhost:8080/v3/api-docs/
    * http://localhost:8080/swagger-ui/index.html
    * /v3/api-docs(search for /v3/api-docs on explore search bar, then will get recipes-web endpoints)
