package evaluation

import scala.concurrent.Future

trait RestClient {
  
	def getPosts: Future[List[Post]]
	
	def getPostForUser(userId: Int): Future[List[Post]]
	
	def createNewPost(post: Post): Future[Post]
	
	def updatePost(post: Post): Future[Post]
	
	def deletePost(id: Int): Future[Boolean]
	
}