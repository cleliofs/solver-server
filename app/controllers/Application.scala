package controllers

import com.codesynergy.solver.CamelConfig
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

/**
 * Created by clelio on 06/05/15.
 */
object Application extends Controller {

  val camelConf = CamelConfig

  def index = Action {
    Ok(Json.toJson(Map("status" -> "OK", "message" -> (s"Hello "))))
  }


}
