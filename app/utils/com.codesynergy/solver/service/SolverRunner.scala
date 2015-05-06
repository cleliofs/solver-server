package com.codesynergy.solver.service

import com.aimia.solver.client.model.Model
import org.apache.camel.{Exchange, Processor}

/**
 * Created by csouza on 23/04/2015.
 */
object SolverRunner extends Processor {

  override def process(exchange: Exchange): Unit = {
    val model = new Model.Builder().build();
    println(s"Running model $model")
    exchange.getIn.setBody(model)
  }

}
