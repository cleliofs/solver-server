package uk.co.codesynergy.solver.service

import java.lang.String._

import com.aimia.solver.client.model._
import gurobi.GRB.StringAttr
import gurobi._

import scala.collection.mutable

/**
 * Created by csouza on 23/04/2015.
 */
object GurobiModelConverter extends ModelConverter[Model, GRBModel] {

  val GRB_ENV: GRBEnv = new GRBEnv()

  override def convert(model: Model): GRBModel = {
    var grbModel: GRBModel = null

    // Model
    grbModel = new GRBModel(GRB_ENV)
    grbModel.set(StringAttr.ModelName, model.getName)

    // Decision variables
    val mutableMap: mutable.HashMap[Variable, GRBVar] = new mutable.HashMap[Variable, GRBVar]
    val variables: List[Variable] = model.getVariables.asInstanceOf[List[Variable]]
    variables.foreach( v => {
      val grbVar: GRBVar = grbModel.addVar(v.getLowerBound, v.getUpperBound, v.getObjective, convertVarType(v.getType), v.getName)
      mutableMap.put(v, grbVar)
    })
    val varToGRBVarMap: Map[Variable, GRBVar] = mutableMap.toMap

    // Update model to integrate new variables
    grbModel.update

    // Constraints
    val constraints: List[Constraint] = model.getConstraints.asInstanceOf[List[Constraint]]
    constraints.foreach {
      convertAndAddConstraintToModel(_, grbModel, varToGRBVarMap)
    }

    // Set Objective
    val objective: Objective = model.getObjective
    if (objective == null) {
      throw new ModelConverterException(format("No objective was passed to the model: %s", model))
    }
    grbModel.setObjective(convertLinearExp("objective", objective.getLinearExpr, varToGRBVarMap), convertObjectiveSense(objective.getSense))

    // Add model parameters (if any)
    convertAndAddModelParam(model.getParam, grbModel)

    // Update model to integrate constraints, objective and model parameters
    grbModel.update

    grbModel
  }

  @throws(classOf[GRBException])
  private def convertAndAddConstraintToModel(c: Constraint, model: GRBModel, varToGRBVarMap: Map[Variable, GRBVar]): GRBConstr = {
    val lhsVar: GRBVar = varToGRBVarMap.get(c.getLhsVar).get
    val rhsVar: GRBVar = varToGRBVarMap.get(c.getRhsVar).get
    val lhsExpr: GRBLinExpr = convertLinearExp(c.getName, c.getLhsExpr, varToGRBVarMap)
    val rhsExpr: GRBLinExpr = convertLinearExp(c.getName, c.getRhsExpr, varToGRBVarMap)
    val lhs: Double = c.getLhsValue
    val rhs: Double = c.getRhsValue
    val sense: Char = convertConstrSense(c.getSense)
    if (!lhs.equals(null)) {
      if (rhsVar == null && rhsExpr != null) {
        return model.addConstr(lhs, sense, rhsExpr, c.getName)
      }
      else if (rhsVar != null && rhsExpr == null) {
        return model.addConstr(lhs, sense, rhsVar, c.getName)
      }
    }
    if (!lhs.equals(null)) {
      if (lhsVar == null && lhsExpr != null) {
        return model.addConstr(lhsExpr, sense, rhs, c.getName)
      }
      else if (lhsVar != null && lhsExpr == null) {
        return model.addConstr(lhsVar, sense, rhs, c.getName)
      }
    }
    if (lhsVar == null && lhsExpr != null) {
      if (rhsVar == null && rhsExpr != null) {
        return model.addConstr(lhsExpr, sense, rhsExpr, c.getName)
      }
      else if (rhsVar != null && rhsExpr == null) {
        return model.addConstr(lhsExpr, sense, rhsVar, c.getName)
      }
    }
    if (lhsVar != null && lhsExpr == null) {
      if (rhsVar == null && rhsExpr != null) {
        return model.addConstr(lhsVar, sense, rhsExpr, c.getName)
      }
      else if (rhsVar != null && rhsExpr == null) {
        return model.addConstr(lhsVar, sense, rhsVar, c.getName)
      }
    }
    throw new ModelConverterException(format("Invalid constraint: %s", c))
  }

  @throws(classOf[GRBException])
  private def convertLinearExp(constraintName: String, linearExpr: LinearExpr, varToGRBVarMap: Map[Variable, GRBVar]): GRBLinExpr = {
    if (linearExpr == null) {
      return null
    }
    val le: GRBLinExpr = new GRBLinExpr
    if (linearExpr.getConstant != null) {
      le.addConstant(linearExpr.getConstant)
    }
    val coefficients: List[Double] = linearExpr.getCoefficients.asInstanceOf[List[Double]]
    if (coefficients.isEmpty) {
      throw new ModelConverterException(format("Coefficients are empty on the linear expression: %s for constraint: %s", linearExpr, constraintName))
    }
    val coeffs: Array[Double] = new Array[Double](coefficients.size)
    var i: Int = 0
    for (c <- coefficients) {
      coeffs(i) = c
      i += 1
    }
    val variables: List[Variable] = linearExpr.getVariables.asInstanceOf[List[Variable]]
    if (variables.isEmpty) {
      throw new ModelConverterException(format("Variables are empty on the linear expression: %s for constraint: %s", linearExpr, constraintName))
    }
    val vars: Array[GRBVar] = new Array[GRBVar](variables.size)
    i = 0
    for (v <- variables) {
      vars(i) = varToGRBVarMap.get(v).get
      i += 1
    }
    le.addTerms(coeffs, vars)
    return le
  }

  private def convertVarType(variableType: Variable.Type): Char = {
    variableType match {
      case Variable.Type.BINARY =>
        return GRB.BINARY
      case Variable.Type.INTEGER =>
        return GRB.INTEGER
      case Variable.Type.SEMI_INTEGER =>
        return GRB.SEMIINT
      case Variable.Type.SEMI_CONTINUOUS =>
        return GRB.SEMICONT
      case _ =>
        return GRB.CONTINUOUS
    }
  }

  private def convertConstrSense(constraintSense: Constraint.Sense): Char = {
    constraintSense match {
      case Constraint.Sense.GREATER_EQUAL =>
        return GRB.GREATER_EQUAL
      case Constraint.Sense.LESS_EQUAL =>
        return GRB.LESS_EQUAL
      case _ =>
        return GRB.EQUAL
    }
  }

  private def convertObjectiveSense(objectiveSense: Objective.Sense): Int = {
    objectiveSense match {
      case Objective.Sense.MINIMIZE =>
        return GRB.MINIMIZE
      case _ =>
        return GRB.MAXIMIZE
    }
  }

  @throws(classOf[GRBException])
  private def convertAndAddModelParam(param: Param, grbModel: GRBModel) {
    if (param == null) {
      return
    }
    if (param.getHeuristics != null) {
      grbModel.getEnv.set(GRB.DoubleParam.Heuristics, param.getHeuristics)
    }
    if (param.getMipFocus != null) {
      grbModel.getEnv.set(GRB.IntParam.MIPFocus, param.getMipFocus)
    }
    if (param.getMipGap != null) {
      grbModel.getEnv.set(GRB.DoubleParam.MIPGap, param.getMipGap)
    }
    if (param.getImproveStartTime != null) {
      grbModel.getEnv.set(GRB.DoubleParam.ImproveStartTime, param.getImproveStartTime)
    }
    if (param.getTimeLimit != null) {
      grbModel.getEnv.set(GRB.DoubleParam.TimeLimit, param.getTimeLimit)
    }
  }
}
