package com.codesynergy.solver

import javax.jms.ConnectionFactory

import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.camel.component.jms.JmsComponent
import org.apache.camel.impl.{SimpleRegistry, DefaultCamelContext}
import org.apache.camel.scala.dsl.builder.{RouteBuilderSupport, ScalaRouteBuilder}
import org.apache.camel.{CamelContext, Exchange}
import com.codesynergy.solver.processor.{SolverExecutionStartProcessor, SolverExecutionResultProcessor}
import com.codesynergy.solver.service.SolverRunner

/**
 * Created by clelio on 22/04/15.
 */
object CamelConfig extends RouteBuilderSupport {

  val registry = new SimpleRegistry
  registry.put("SolverRunner", SolverRunner)
  val context: CamelContext = new DefaultCamelContext(registry)

  val connectionFactory: ConnectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616")
  context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory))
  val routeBuilder = new ScalaRouteBuilder(context) {
    from("jms:queue:solver.IN")
      .process(SolverExecutionStartProcessor)
//      .process(SolverRunner)
      .process(SolverExecutionResultProcessor)
    .stop

  }

  context.addRoutes(routeBuilder)

  def start = context.start
}
