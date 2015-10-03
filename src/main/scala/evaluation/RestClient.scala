package evaluation

import scala.concurrent.Future

trait RestClient {
  
	def getPosts: Future[List[Post]]
	
	def getPostForUser(userId: Int): Future[List[Post]]
	
	def createNewPost(post: Post): Future[CreatePostResponse]
	
	def updatePost(id: Int, post: Post): Future[Post]
	
	def deletePost(id: Int): Future[Boolean]
	
}