package evaluation.dispatch

import org.scalatest.FlatSpec
import evaluation.RestClient
import evaluation.Post
import evaluation.TestObjects._
import org.scalatest._

trait RestClientBehavior extends Matchers { this: FlatSpec =>

	def restClient(client: => RestClient) {

		it should "get posts via GET" in {
			val posts: List[Post] = client.getPosts

			posts should have length POSTS_COUNT
			posts(0) should equal(FIRST_POST)
		}

		it should "get posts via GET with query parameter" in {
			val posts: List[Post] = client.getPostForUser(1)

			posts should have length POSTS_FOR_USER_1_COUNT
			posts(0) should equal(FIRST_POST)
		}

		it should "create a new post via PUT" in {
			val newPost = Post(id = 0, userId = 1, title = "new post", body = "test")

			val result = client.createNewPost(newPost)

			result.id should equal(POSTS_COUNT + 1)
			result.userId should equal(1)
			result.title should equal("new post")
			result.body should equal("test")
		}

		it should "update a post via POST" in {
			val updatedPost = FIRST_POST.copy(body = "updated")

			val result = client.createNewPost(updatedPost)

			result.id should equal(FIRST_POST.id)
			result.userId should equal(FIRST_POST.userId)
			result.title should equal(FIRST_POST.title)
			result.body should equal("updated")
		}

		it should "delete a post via DELETE" in {
			val deleted = client.deletePost(0)

			deleted should equal(true)
		}

	}

}