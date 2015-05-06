package com.codesynergy.solver.service

import com.aimia.solver.client.model.Model
import org.apache.commons.lang.SerializationUtils

/**
 * Created by csouza on 23/04/2015.
 */
trait PayloadConvert {
  def convert(b: Array[Byte]): Model = SerializationUtils.deserialize(b).asInstanceOf[Model]

}
