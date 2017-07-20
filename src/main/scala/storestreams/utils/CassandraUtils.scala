package storestreams.utils

import java.net.InetAddress

import com.datastax.driver.core.{Cluster, Session}

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer

object CassandraUtils {
  private var cluster: Cluster = null
  private var session: Session = null

  def connect(nodes: List[InetAddress], port: Int): (Cluster, Session) = {
    cluster = Cluster.builder().addContactPoints(nodes).withPort(port).build()
    session = cluster.connect()
    (cluster, session)
  }

  def getSession() = {
    session
  }

  def close() = {
    if (session != null) session.close()
    if (cluster != null) cluster.close()
  }

  private def createCassandraIPList(hosts: List[String]): ArrayBuffer[InetAddress] = {
    var casIPList = ArrayBuffer[InetAddress]()
    for (casIP <- hosts)
      casIPList += InetAddress.getByName(casIP.asInstanceOf[String])
    casIPList
  }
}