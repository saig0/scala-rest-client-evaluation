package evaluation.spring

import evaluation.RestService
import evaluation.RestClient
import evaluation.Post
import scala.concurrent.{ Future, Promise }
import scala.concurrent.ExecutionContext.Implicits.global
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.client.RestTemplate
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.springframework.web.client.AsyncRestTemplate
import org.springframework.util.concurrent.ListenableFutureCallback
import org.springframework.http.ResponseEntity
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import evaluation.CreatePostResponse
import org.springframework.web.util.UriComponentsBuilder
import java.lang.reflect.{ Type, ParameterizedType }
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.`type`.TypeReference
import evaluation.CreatePostResponse
import org.springframework.util.concurrent.ListenableFuture
import evaluation.CreatePostResponse
import evaluation.CreatePostResponse

/**
 * @author Philipp
 */
class SpringClient extends RestClient {

	val mapper = new ObjectMapper
	mapper.registerModule(DefaultScalaModule)

	val template = new AsyncRestTemplate

	val url = "http://" + RestService.postUrl

	implicit def objectToHttpEntity(x: Any) = new HttpEntity(x)

	def getPosts: Future[List[Post]] = {
		val response = template.getForEntity(url, classOf[String])
		handleResponse(response, (body: String) => deserialize[List[Post]](body))
	}

	def getPostForUser(userId: Int): Future[List[Post]] = {

		val builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("userId", userId.toString)
		val urlWithParam = builder.build.encode.toUri

		val response = template.getForEntity(urlWithParam, classOf[String])
		handleResponse(response, (body: String) => deserialize[List[Post]](body))
	}

	def createNewPost(post: Post): Future[CreatePostResponse] = {

		val json = mapper.writeValueAsString(post)
		val response = template.postForEntity(url, json, classOf[String])
		handleResponse(response, (body: String) => deserialize[CreatePostResponse](body))
	}

	def updatePost(id: Int, post: Post): Future[Post] = {

		val headers = new HttpHeaders()
		headers.setContentType(MediaType.APPLICATION_JSON)

		val json = mapper.writeValueAsString(post)
		val entity = new HttpEntity[String](json, headers)

		val response = template.exchange(s"$url/${id.toString}", HttpMethod.PUT, entity, classOf[String])
		handleResponse(response, (body: String) => deserialize[Post](body))
	}

	def deletePost(id: Int): Future[Boolean] = {

		val response = template.delete(s"$url/${id.toString}")		
		handleResponseWithoutBody(response, () => true)
	}

	private def handleResponse[T, V](response: ListenableFuture[ResponseEntity[V]], f: V => T): Future[T] = {
		val promise = Promise[T]

		response.addCallback(
			new ListenableFutureCallback[ResponseEntity[V]]() {

				def onSuccess(entity: ResponseEntity[V]) {
					val body = f(entity.getBody)
					promise success body
				}

				def onFailure(cause: Throwable) {
					promise failure cause
				}

			})

		promise.future
	}
	
	private def handleResponseWithoutBody[T](response: ListenableFuture[_], f: () => T): Future[T] = {
		val promise = Promise[T]

		response.asInstanceOf[ListenableFuture[ResponseEntity[Unit]]].addCallback(
			new ListenableFutureCallback[ResponseEntity[Unit]]() {

				def onSuccess(entity: ResponseEntity[Unit]) {
					val body = f()
					promise success body
				}

				def onFailure(cause: Throwable) {
					promise failure cause
				}

			})

		promise.future
	}

	private def deserialize[T: Manifest](value: String): T =
		mapper.readValue(value, typeReference[T])

	private[this] def typeReference[T: Manifest] = new TypeReference[T] {
		override def getType = typeFromManifest(manifest[T])
	}

	// need to de-serialize List[Post]
	// mapper.readValue(json, classOf[List[Post]]) -> List[Map[String,Any]]
	private[this] def typeFromManifest(m: Manifest[_]): Type = {
		if (m.typeArguments.isEmpty) { m.runtimeClass }
		else new ParameterizedType {
			def getRawType = m.runtimeClass

			def getActualTypeArguments = m.typeArguments.map(typeFromManifest).toArray

			def getOwnerType = null
		}
	}

}