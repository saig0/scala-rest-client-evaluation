package evaluation.dispatch

import dispatch._
import dispatch.Defaults._
import evaluation.RestService
import net.liftweb.json._
import net.liftweb.json.Serialization._
import evaluation.Post
import com.ning.http.client.RequestBuilder
import evaluation.RestClient
import evaluation.CreatePostResponse

class DispatchClient extends RestClient {
  
	private implicit val formats = DefaultFormats
	
	val jsonHeader = Map("Content-Type" -> "application/json")
	
	val url = host(RestService.postUrl)
	
	def getPosts: Future[List[Post]] = {
		handleRequest(url GET, json => 
			parse(json).extract[List[Post]])
	}	
	
	def getPostForUser(userId: Int): Future[List[Post]] = {
		val request = url GET
		val requstWithQueryParameter = url <<? Map("userId" -> userId.toString)
		// val requstWithQueryParameter = request.addQueryParameter("userId", userId.toString)
		
		handleRequest(requstWithQueryParameter, json => 
			parse(json).extract[List[Post]])
	}
	
	def createNewPost(post: Post): Future[CreatePostResponse] = {
		val json: String = write(post)		
		val request = jsonRequest(url POST, json)
		
		handleRequest(request, json => 
			parse(json).extract[CreatePostResponse])
	}
	
	def updatePost(id: Int, post: Post): Future[Post] = {
		val json: String = write(post)		
		val request = jsonRequest(url / id.toString PUT, json)
		
		handleRequest(request, json => 
			parse(json).extract[Post])
	}
	
	def deletePost(id: Int): Future[Boolean] = {
		val request = url / id.toString DELETE
		
		handleRequest(request, _ => (true))
	}
	
	private def jsonRequest(request: Req, json: String): Req = {
		request << json <:< jsonHeader
		// request.setContentType("application/json", "UTF-8") << json
	}

	private def handleRequest[T](request: Req, handler: String => T): Future[T] =
		for (result <- Http(request OK as.String).either) yield {
			result match {
				case Right(content) => handler(content)
				case Left(StatusCode(404)) => throw new RuntimeException("request failed: not found (404)")
				case Left(StatusCode(code)) => throw new RuntimeException("request failed: " + code)
				case Left(e) => throw new RuntimeException("request failed: " + e)
			}
		}
	
}