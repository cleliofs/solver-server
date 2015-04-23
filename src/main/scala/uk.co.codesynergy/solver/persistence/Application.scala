package uk.co.codesynergy.solver.persistence

import javax.jms.ConnectionFactory

import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.camel.CamelContext
import org.apache.camel.LoggingLevel._
import org.apache.camel.component.jms.JmsComponent
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.scala.dsl.builder.{RouteBuilderSupport, ScalaRouteBuilder}
import uk.co.codesynergy.solver.processor.{SolverExecutionResultProcessor, SolverExecutionStartProcessor}
import uk.co.codesynergy.solver.service.ExchangePayloadExtractor

/**
 * Created by csouza on 23/04/2015.
 */
object Application extends App with RouteBuilderSupport {

  val context: CamelContext = new DefaultCamelContext
  val connectionFactory: ConnectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616")
  context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory))
  val routeBuilder = new ScalaRouteBuilder(context) {

    // Solver IN queue to process optimisation
    from("jms:queue:solver.IN").routeId("SolverRunnerRoute")
      .log(INFO, "Received message from solver.IN queue")
    .process(SolverExecutionStartProcessor)
      .log(INFO, "Solver execution started...")
//  .unmarshal(new DataFormatDefinition).gzip()
    .bean(ExchangePayloadExtractor, "extract")
      .log(INFO, "Message body extracted")
    .bean(ExchangePayloadExtractor, "convert")
      .log(INFO, "Exchange type converted to model")
    .to("bean:solverRunner?method=run")
      .log(INFO, "Solver runner completed")
    .process(SolverExecutionResultProcessor)
      .log(INFO, "Solver execution saved...")
      .log(DEBUG, "${body}")
    .stop
//  .choice()
//      .when(header(JmsConfig.CAMEL_JMS_MESSAGE_GROUP_ID).isNotNull())
//      .log(INFO, format("Sending message back to %s queue for message group %s ",
//      jmsConfig.getSolverOutQueue(), simple(format("${header.%s}", CAMEL_JMS_MESSAGE_GROUP_ID))))
//      .toF("jms:queue:%s", jmsConfig.getSolverOutQueue())
//      .end()
//    .end();
  }

  context.addRoutes(routeBuilder)
  context.start
  while (true) {}

}
