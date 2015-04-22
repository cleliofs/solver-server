package uk.co.codesynergy

import javax.jms.ConnectionFactory

import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.camel.{Exchange, CamelContext}
import org.apache.camel.component.jms.JmsComponent
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.scala.dsl.builder.{ScalaRouteBuilder, RouteBuilderSupport}

/**
 * Created by clelio on 22/04/15.
 */
object RoutingAfterCBRExample extends App with RouteBuilderSupport {

  private def fileEndsWith(ex: Exchange, fileExt: String): Boolean = {
    ex.getIn.getHeader("CamelFileName") match {
      case x: String => return x.endsWith(fileExt)
      case _ => false
    }
  }

  val context: CamelContext = new DefaultCamelContext
  val connectionFactory: ConnectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616")
  context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory))
  val routeBuilder = new ScalaRouteBuilder(context) {
    from("file:src/test/resources/inbox")
      .choice {
        when(fileEndsWith(_, "xml")) --> ("jms:xmlOrders")
        when(fileEndsWith(_, "csv")) --> ("jms:csvOrders")
        otherwise() --> ("jms:unknownOrders")
    }

    from("jms:xmlOrders")
      .process(exchange => println(s"XML type order received: ${exchange.getIn().getHeader("CamelFileName")}"))
    from("jms:csvOrders")
      .process(exchange => println(s"CSV type order received: ${exchange.getIn().getHeader("CamelFileName")}"))
    from("jms:unknownOrders")
      .process(exchange => println(s"Unknown type order received: ${exchange.getIn().getHeader("CamelFileName")}"))

  }

  context.addRoutes(routeBuilder)
  context.start
  while (true) {}
}
