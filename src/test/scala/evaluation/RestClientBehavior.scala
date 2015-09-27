package evaluation.dispatch

import org.scalatest.FlatSpec
import evaluation.RestClient
import evaluation.Post
import evaluation.TestObjects._
import org.scalatest._
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.Await

trait RestClientBehavior extends Matchers { this: FlatSpec =>

	def restClient(client: => RestClient) {

		it should "get posts via GET" in {

			whenReady(client.getPosts) { posts =>

				posts should have length POSTS_COUNT
				posts(0) should equal(FIRST_POST)
			}
		}

		it should "get posts via GET with query parameter" in {

			whenReady(client.getPostForUser(1)) { posts =>

				posts should have length POSTS_FOR_USER_1_COUNT
				posts(0) should equal(FIRST_POST)
			}
		}

		it should "create a new post via PUT" in {
			val newPost = Post(id = 0, userId = 1, title = "new post", body = "test")

			whenReady(client.createNewPost(newPost)) { result =>

				result.id should equal(POSTS_COUNT + 1)
				result.userId should equal(1)
				result.title should equal("new post")
				result.body should equal("test")
			}
		}

		it should "update a post via POST" in {
			val updatedPost = FIRST_POST.copy(body = "updated")

			whenReady(client.createNewPost(updatedPost)) { result =>

				result.id should equal(FIRST_POST.id)
				result.userId should equal(FIRST_POST.userId)
				result.title should equal(FIRST_POST.title)
				result.body should equal("updated")
			}
		}

		it should "delete a post via DELETE" in {

			whenReady(client.deletePost(0)) { deleted =>

				deleted should equal(true)
			}
		}

	}

	// somehow org.scalatest.concurrent.Futures do not work
	private def whenReady[T](future: Future[T])(test: T => Unit) = {
		val result = Await.result(future, 1 second)
		test(result)
	}

}