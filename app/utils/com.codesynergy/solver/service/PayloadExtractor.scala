package com.codesynergy.solver.service

import org.apache.camel.Exchange

/**
 * Created by csouza on 23/04/2015.
 */
trait PayloadExtractor {
  def extract(ex: Exchange): Array[Byte] = ex.getIn().getBody().asInstanceOf[Array[Byte]]

}
