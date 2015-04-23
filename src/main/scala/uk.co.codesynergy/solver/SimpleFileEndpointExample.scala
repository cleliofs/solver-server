package uk.co.codesynergy.solver

import org.apache.camel.CamelContext
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.scala.dsl.builder.ScalaRouteBuilder

/**
 * Example taken from http://www.hascode.com/2013/02/using-apache-camel-with-scala-and-the-camel-scala-dsl/
 *
 * Created by clelio on 22/04/15.
 */
object SimpleFileEndpointExample extends App {
  val context: CamelContext = new DefaultCamelContext
  val routeBuilder = new ScalaRouteBuilder(context) {
    from("file:data/inbox") --> ("file:data/outbox")
  }
  context.addRoutes(routeBuilder)
  context.start
  while (true) {}
}
