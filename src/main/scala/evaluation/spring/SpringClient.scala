package evaluation.spring

import evaluation.RestService
import evaluation.RestClient
import evaluation.Post
import scala.concurrent.Future
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
import java.lang.reflect.{Type, ParameterizedType}
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.`type`.TypeReference; 

/**
 * @author Philipp
 */
class SpringClient extends RestClient {

	val mapper = new ObjectMapper
	mapper.registerModule(DefaultScalaModule)

	val template = new RestTemplate // new AsyncRestTemplate

	val url = "http://" + RestService.postUrl

	def getPosts: Future[List[Post]] = {

		Future {
			val response = template.getForObject(url, classOf[String])
			mapper.readValue(response, classOf[List[Post]])
			deserialize[List[Post]](response)
		}

		//		val callback = new ListenableFutureCallback[ResponseEntity[List[Post]]](){
		//			
		//		}
		//		
		//		template.getForEntity(uri, classOf[List[Post]]).addCallback(callback)

	}

	def getPostForUser(userId: Int): Future[List[Post]] = {
		Future {
			val builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("userId", userId.toString)
			val urlWithParam = builder.build.encode.toUri
			
			val response = template.getForObject(urlWithParam, classOf[String])
			deserialize[List[Post]](response)
		}
	}

	def createNewPost(post: Post): Future[CreatePostResponse] = {
		Future {
			val json = mapper.writeValueAsString(post)
			val response = template.postForObject(url, json, classOf[String])
			deserialize[CreatePostResponse](response)
		}
	}

	def updatePost(id: Int, post: Post): Future[Post] = {
		Future {
			val headers = new HttpHeaders()
			headers.setContentType(MediaType.APPLICATION_JSON)
			
			val json = mapper.writeValueAsString(post)
			val entity = new HttpEntity[String](json, headers)
			
			val response = template.exchange(s"$url/${id.toString}", HttpMethod.PUT, entity, classOf[String]).getBody
			deserialize[Post](response)
		}
	}

	def deletePost(id: Int): Future[Boolean] = {
		Future {
			template.delete(s"$url/${id.toString}")
			true
		}
	}
	
	private def deserialize[T: Manifest](value: String) : T =
    mapper.readValue(value, typeReference[T])

  private [this] def typeReference[T: Manifest] = new TypeReference[T] {
    override def getType = typeFromManifest(manifest[T])
  }

	// need to de-serialize List[Post]
	// mapper.readValue(json, classOf[List[Post]]) -> List[Map[String,Any]]
  private [this] def typeFromManifest(m: Manifest[_]): Type = {
    if (m.typeArguments.isEmpty) { m.runtimeClass }
    else new ParameterizedType {
      def getRawType = m.runtimeClass

      def getActualTypeArguments = m.typeArguments.map(typeFromManifest).toArray

      def getOwnerType = null
    }
  }

}