import com.codesynergy.solver.CamelConfig
import play.api.{Application, GlobalSettings, Logger}

/**
 * Created by clelio on 06/05/15.
 */
object Global extends GlobalSettings {

  override def onStart(app: Application): Unit = {
    val camelConfig = CamelConfig
    camelConfig.start
    Logger.info("Application has started and camel routes initiated...")
  }
}
