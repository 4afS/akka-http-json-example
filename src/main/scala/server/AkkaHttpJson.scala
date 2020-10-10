package server

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.Http

import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import java.util._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport

case class Person(name: String, age: Int)
case class UserAdded(id: String, timestamp: Long)

trait PersonJsonProtocol extends DefaultJsonProtocol {
  implicit val personFormat = jsonFormat2(Person)
  implicit val userAddedFormat = jsonFormat2(UserAdded)
}

// object AkkaHttpJson extends PersonJsonProtocol with SprayJsonSupport {
//   implicit val system = ActorSystem(Behaviors.empty, "AkkaHttpJson")

//   val route: Route = (path("api" / "user") & post) {
//     entity(as[Person]) { person: Person =>
//       println(person)
//       complete(UserAdded(UUID.randomUUID().toString(), System.currentTimeMillis()))
//     }
//   }

//   def main(args: Array[String]): Unit = {
//     Http().newServerAt("localhost", 8080).bind(route)
//   }
// }

object AkkaHttpCirce extends FailFastCirceSupport {
  import io.circe.generic.auto._

  implicit val system = ActorSystem(Behaviors.empty, "AkkaHttpJson")

  val route: Route = (path("api" / "user") & post) {
    entity(as[Person]) { person: Person =>
      println(person)
      complete(
        UserAdded(UUID.randomUUID().toString(), System.currentTimeMillis())
      )
    }
  }

  def main(args: Array[String]): Unit = {
    Http().newServerAt("localhost", 8080).bind(route)
  }
}
