package evaluation.scalaws

import evaluation.RestClient
import evaluation.Post
import play.api.Play.current
import play.api.libs.ws._
import play.api.libs.ws.ning.NingAsyncHttpClientConfigBuilder
import scala.concurrent.Future
import evaluation.RestService
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global

class ScalaWsClient extends RestClient {

	val url = "http://" + RestService.postUrl

	val builder = new com.ning.http.client.AsyncHttpClientConfig.Builder()
	val client = new play.api.libs.ws.ning.NingWSClient(builder.build())

	implicit val postReads = Json.reads[Post]
	implicit val postWrites = Json.writes[Post]

	def getPosts: Future[List[Post]] = {
		extractJson[List[Post]](
			client.url(url)
				.get()
		)
	}

	def getPostForUser(userId: Int): Future[List[Post]] = {
		extractJson[List[Post]](
			client.url(url)
				.withQueryString("userId" -> userId.toString)
				.get
		)
	}

	def createNewPost(post: Post): Future[Post] = {
		extractJson[Post](
			client.url(url)
				.post(Json.toJson(post))
		)
	}

	def updatePost(post: Post): Future[Post] = {
		extractJson[Post](
			client.url(url)
				.put(Json.toJson(post))
		)
	}

	def deletePost(id: Int): Future[Boolean] = {
		client.url(s"$url/${id.toString}")
			.delete() map (_ => true)
	}

	private def extractJson[T](futureResponse: Future[WSResponse])(implicit reader: Reads[T]): Future[T] = {
		futureResponse.map {
			response =>
				(response.json).validate[T] match {
					case s: JsSuccess[T] => s.get
					case e: JsError => throw new RuntimeException("Failed to extract JSON: " + JsError.toFlatJson(e))
				}
		}
	}

}