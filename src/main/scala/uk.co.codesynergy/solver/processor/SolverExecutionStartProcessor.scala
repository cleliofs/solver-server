package uk.co.codesynergy.solver.processor

import org.apache.camel.{Exchange, Processor}

/**
 * Created by csouza on 23/04/2015.
 */
object SolverExecutionStartProcessor extends Processor {
  override def process(ex: Exchange): Unit = println("Sending message to inform optimisation has started...")


}
