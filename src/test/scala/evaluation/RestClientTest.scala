package evaluation

import org.scalatest._
import evaluation.dispatch.RestClientBehavior
import evaluation.dispatch.DispatchClient
import evaluation.scalaws.ScalaWsClient
import evaluation.spring.SpringClient

class RestClientTest extends FlatSpec with RestClientBehavior {
  
	"Dispath client" should behave like restClient(new DispatchClient)
	
	"ScalaWS client" should behave like restClient(new ScalaWsClient)
	
	"Spring client" should behave like restClient(new SpringClient)
	
}