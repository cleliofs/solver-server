package com.codesynergy.solver.processor

import org.apache.camel.{Exchange, Processor}

/**
 * Created by csouza on 23/04/2015.
 */
object SolverExecutionResultProcessor extends Processor {
  override def process(exchange: Exchange): Unit = println("Notifying a result has been produced...")
}
