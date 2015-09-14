package evaluation

import org.scalatest._
import evaluation.dispatch.RestClientBehavior
import evaluation.dispatch.DispatchClient

class RestClientTest extends FlatSpec with RestClientBehavior {
  
	"Dispath client" should behave like restClient(new DispatchClient)
	
}