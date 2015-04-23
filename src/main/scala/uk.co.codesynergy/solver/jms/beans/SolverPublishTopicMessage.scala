package uk.co.codesynergy.solver.jms.beans

import uk.co.codesynergy.solver.OptimisationStatus
import OptimisationStatus._

/**
 * Created by csouza on 23/04/2015.
 */
case class SolverPublishTopicMessage(applicationId: String,
                                      username: String,
                                      modelName: String,
                                      correlationId: String,
                                      status: String,
                                      message: String,
                                      executionParameters: String,
                                      optimisationStatus: OptimisationStatus,
                                      infeasibleConstraints: String) {


//  def build(exchange: Exchange): Unit = {
//    val in: Message = exchange.getIn
//    val body: Object = in.getBody
//    val optimisationStatus: OptimisationStatus = ((Object[]) body)[0]
//
//
//    SolverPublishTopicMessage(applicationId, username, modelName, correlationId, status, message, executionParameters, optimisationStatus, infeasibleConstraints)
//  }
}
