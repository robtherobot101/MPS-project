package ArduinoML.constraints;

/*Generated by MPS */

import jetbrains.mps.smodel.runtime.base.BaseConstraintsDescriptor;
import jetbrains.mps.smodel.adapter.structure.MetaAdapterFactory;
import java.util.Map;
import org.jetbrains.mps.openapi.language.SProperty;
import jetbrains.mps.smodel.runtime.PropertyConstraintsDescriptor;
import java.util.HashMap;
import jetbrains.mps.smodel.runtime.base.BasePropertyConstraintsDescriptor;
import jetbrains.mps.smodel.adapter.ids.MetaIdFactory;
import org.jetbrains.mps.openapi.model.SNode;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SPropertyOperations;

public class Sensor_Constraints extends BaseConstraintsDescriptor {
  public Sensor_Constraints() {
    super(MetaAdapterFactory.getConcept(0x6d84bad7eb044249L, 0x8d80c9157947c3f2L, 0x2713779fbb61b575L, "ArduinoML.structure.Sensor"));
  }

  @Override
  protected Map<SProperty, PropertyConstraintsDescriptor> getSpecifiedProperties() {
    Map<SProperty, PropertyConstraintsDescriptor> properties = new HashMap<SProperty, PropertyConstraintsDescriptor>();
    properties.put(MetaAdapterFactory.getProperty(0x6d84bad7eb044249L, 0x8d80c9157947c3f2L, 0x2713779fbb61b575L, 0x2713779fbb61b587L, "pin"), new BasePropertyConstraintsDescriptor(MetaIdFactory.propId(0x6d84bad7eb044249L, 0x8d80c9157947c3f2L, 0x2713779fbb61b575L, 0x2713779fbb61b587L), this) {
      @Override
      public boolean hasOwnValidator() {
        return true;
      }
      @Override
      public boolean validateValue(SNode node, String propertyValue) {
        String propertyName = "pin";
        return (SPropertyOperations.getInteger(propertyValue)) >= 1 && (SPropertyOperations.getInteger(propertyValue)) <= 13;
      }
    });
    return properties;
  }
}
