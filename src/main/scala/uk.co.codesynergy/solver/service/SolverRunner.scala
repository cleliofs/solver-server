package uk.co.codesynergy.solver.service

import com.aimia.solver.client.model.Model

/**
 * Created by csouza on 23/04/2015.
 */
object SolverRunner {

  def run(model: Model): Model = {

    new Model.Builder().build();
  }
}
