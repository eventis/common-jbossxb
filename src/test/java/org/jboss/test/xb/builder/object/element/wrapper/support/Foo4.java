package org.jboss.test.xb.builder.object.element.wrapper.support;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Foo4
{
   private Number[] items;

   public Number[] getItems()
   {
      return items;
   }

   @XmlElementWrapper(name="bar")
   @XmlElements({
      @XmlElement(name="int", required=false, type=Integer.class),
      @XmlElement(name="float", required=false, type=Float.class),
      @XmlElement(name="x", required=false, type=MyNumber.class)
   })
   public void setItems(Number[] items)
   {
      this.items = items;
   }
}
