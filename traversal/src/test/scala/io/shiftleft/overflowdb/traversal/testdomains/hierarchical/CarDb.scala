package io.shiftleft.overflowdb.traversal.testdomains.hierarchical

import io.shiftleft.overflowdb.traversal.{NodeOps, Traversal}
import io.shiftleft.overflowdb.{NodeRef, OdbNode, OdbNodeProperty}
import org.apache.tinkerpop.gremlin.structure.{Direction, VertexProperty}
import org.apache.tinkerpop.gremlin.util.iterator.IteratorUtils

class CarDb(ref: NodeRef[CarDb]) extends OdbNode(ref) with NodeOps {
  private var _name: String = null

  def name: String = _name

  override def valueMap = {
    val properties = new java.util.HashMap[String, Any]
    if (_name != null) properties.put(Car.PropertyNames.Name, _name)
    properties
  }

  override protected def specificProperties[V](key: String) =
    key match {
      case Car.PropertyNames.Name if _name != null =>
        IteratorUtils.of(new OdbNodeProperty(this, key, _name.asInstanceOf[V]))
      case _ =>
        java.util.Collections.emptyIterator
    }

  override protected def updateSpecificProperty[V](cardinality: VertexProperty.Cardinality, key: String, value: V) =
    key match {
      case Car.PropertyNames.Name =>
        _name = value.asInstanceOf[String]
        property(Car.PropertyNames.Name)
      case _ =>
        throw new RuntimeException("property with key=" + key + " not (yet) supported by " + this.getClass().getName());
    }

  override protected def removeSpecificProperty(key: String) =
    key match {
      case Car.PropertyNames.Name => _name = null
      case _ =>
        throw new RuntimeException("property with key=" + key + " not (yet) supported by " + this.getClass().getName());
    }

  override protected def layoutInformation = Car.layoutInformation

  override def toString = s"CarDb(id=${ref.id}, name=$name)"
}
