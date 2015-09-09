package evaluation.dispatch

import org.scalatest._
import evaluation.Post
import evaluation.TestObjects._

class DispatchTest extends FlatSpec with Matchers {
  
	val client = new DispatchClient
	
	"Dispath" should "get posts via GET" in {
    val posts: List[Post] = client.getPosts
    
    posts should have length POSTS_COUNT
    posts(0) should equal (FIRST_POST) 
  }
	
}