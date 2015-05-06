package com.codesynergy.solver.service

import com.aimia.solver.client.model.Model
import gurobi.GRBModel

/**
 * Created by csouza on 23/04/2015.
 */
trait ModelConverter[M <: Model, G <: GRBModel] {

  def convert(model: M): G

}
