package com.codesynergy.solver

import akka.actor.{Props, ActorSystem, Actor}
import akka.camel.{CamelMessage, Consumer}


class SimpleFileEndpointExampleWithActor extends Actor with Consumer {
  override def endpointUri: String = "file:data/inbox"

  override def receive: Receive = {
    case msg: CamelMessage => println(s"Received: $msg")
  }

}

object SimpleFileEndpointExampleWithActor extends App {
  val system = ActorSystem("Main")
  val a = system.actorOf(Props[SimpleFileEndpointExampleWithActor])
  while (true) {}
}
