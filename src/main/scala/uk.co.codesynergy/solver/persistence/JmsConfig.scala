package uk.co.codesynergy.solver.persistence

/**
 * Created by csouza on 23/04/2015.
 */
object JmsConfig {
  val JMS_MESSAGE_ID_HEADER: String = "JMSMessageID"
  val APPLICATION_ID_PARAM: String = "applicationId"
  val USER_ID_PARAM: String = "userId"
  val MODEL_NAME_PARAM: String = "modelName"
  val USER_SCENARIO_ID_PARAM: String = "userScenarioId"
  val SCENARIO_VERSION_ID_PARAM: String = "scenarioVersionId"
  val TASK_ID_PARAM: String = "taskId"
  val SOLVER_PUBLISH_ENDPOINT: String = "solver-publish"
  val CAMEL_JMS_MESSAGE_GROUP_ID: String = "JMSXGroupID"
}
