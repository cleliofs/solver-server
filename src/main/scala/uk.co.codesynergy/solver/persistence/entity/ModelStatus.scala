package uk.co.codesynergy.solver.persistence.entity

/**
 * Created by csouza on 23/04/2015.
 */
object ModelStatus extends Enumeration {
  type ModelStatus = Value
  val LOADED,
      OPTIMAL,
      INFEASIBLE,
      INF_OR_UNBD,
      UNBOUNDED,
      CUTOFF,
      ITERATION_LIMIT,
      NODE_LIMIT,
      TIME_LIMIT,
      SOLUTION_LIMIT,
      INTERRUPTED,
      NUMERIC,
      SUBOPTIMAL,
      INTERNAL_ERROR,
      UNKNOWN = Value

}
