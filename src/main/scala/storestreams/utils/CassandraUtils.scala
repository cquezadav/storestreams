package storestreams.utils

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Session

import storestreams.config.ApplicationSettings
import java.net.InetAddress
import scala.collection.mutable.ArrayBuffer
import scala.collection.JavaConversions
import scala.collection.JavaConversions._
import com.typesafe.config.ConfigValue
import com.typesafe.config.ConfigList

object CassandraUtils {
  private var cluster: Cluster = null
  private var session: Session = null

  def connect(nodes: List[InetAddress], port: Int): (Cluster, Session) = {
    cluster = Cluster.builder().addContactPoints(nodes).withPort(port).build()
    session = cluster.connect()
    (cluster, session)
  }

  def close() = {
    if (session != null) session.close()
    if (cluster != null) cluster.close()
  }

  def getSession() = {
    session
  }

  def createCassandraIPList(hosts: List[String]): ArrayBuffer[InetAddress] =
    {
      var casIPList = ArrayBuffer[InetAddress]()
      for (casIP <- hosts)
        casIPList += InetAddress.getByName(casIP.asInstanceOf[String])
      casIPList;
    }

}