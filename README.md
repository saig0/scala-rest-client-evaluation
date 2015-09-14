# scala-rest-client-evaluation
evaluation of rest client frameworks for scala

# Frameworks:
* [Dispatch](http://dispatch.databinder.net)
* [ScalaWS](https://www.playframework.com/documentation/2.3.x/ScalaWS)
* [Newman](https://github.com/stackmob/newman)
* [Spring](http://projects.spring.io/spring-framework/)

# How to run:
 
 Run the unit tests with [SBT](http://www.scala-sbt.org/):
 
 ```
 sbt test
 ```
 
 The tests make rest calls against a [fake online rest api](http://jsonplaceholder.typicode.com).
 
# Dispatch
 
 Call url via GET and parse json response. JSON is parsed by [Lift JSON](https://github.com/lift/framework/tree/master/core/json).
 
 ```scala
 def getPosts: List[Post] = {
		val request = url(RestService.postUrl) GET
		val result = Http(request OK as.String)
		
		val json = parse(result())
		json.extract[List[Post]]
	}
 ```
