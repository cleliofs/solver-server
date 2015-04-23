package uk.co.codesynergy.solver.persistence.entity

import java.util.Date

import uk.co.codesynergy.solver.persistence.entity.ModelStatus.ModelStatus

/**
 * Created by csouza on 23/04/2015.
 */
case class SolverExecution(id: Long,
                           applicationId: String,
                            userId: String,
                            modelName: String,
                            jmsCorrelationId: String,
                            status: ModelStatus,
                            startDate: Date,
                            endDate: Date,
                            parameters: String,
                            infeasibleConstraints: String) {

}
