package evaluation

trait RestClient {
  
	def getPosts: List[Post]
	
	def getPostForUser(userId: Int): List[Post]
	
	def createNewPost(post: Post): Post
	
	def updatePost(post: Post): Post
	
	def deletePost(id: Int): Boolean
}