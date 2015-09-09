package evaluation.dispatch

import dispatch._
import dispatch.Defaults._
import evaluation.RestService
import net.liftweb.json._
import net.liftweb.json.Serialization._
import evaluation.Post

class DispatchClient {
  
	private implicit val formats = DefaultFormats
	
	def getPosts: List[Post] = {
		val request = url(RestService.postUrl)
		val result = Http(request OK as.String)
		
		val json = parse(result())
		json.extract[List[Post]]
	}	
	
}