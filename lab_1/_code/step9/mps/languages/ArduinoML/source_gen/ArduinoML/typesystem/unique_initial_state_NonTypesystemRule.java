package ArduinoML.typesystem;

/*Generated by MPS */

import jetbrains.mps.lang.typesystem.runtime.AbstractNonTypesystemRule_Runtime;
import jetbrains.mps.lang.typesystem.runtime.NonTypesystemRule_Runtime;
import org.jetbrains.mps.openapi.model.SNode;
import jetbrains.mps.typesystem.inference.TypeCheckingContext;
import jetbrains.mps.lang.typesystem.runtime.IsApplicableStatus;
import jetbrains.mps.internal.collections.runtime.ListSequence;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SLinkOperations;
import jetbrains.mps.smodel.adapter.structure.MetaAdapterFactory;
import jetbrains.mps.internal.collections.runtime.IWhereFilter;
import jetbrains.mps.lang.smodel.generator.smodelAdapter.SPropertyOperations;
import jetbrains.mps.errors.messageTargets.MessageTarget;
import jetbrains.mps.errors.messageTargets.NodeMessageTarget;
import jetbrains.mps.errors.IErrorReporter;
import org.jetbrains.mps.openapi.language.SAbstractConcept;

public class unique_initial_state_NonTypesystemRule extends AbstractNonTypesystemRule_Runtime implements NonTypesystemRule_Runtime {
  public unique_initial_state_NonTypesystemRule() {
  }
  public void applyRule(final SNode app, final TypeCheckingContext typeCheckingContext, IsApplicableStatus status) {
    if (ListSequence.fromList(SLinkOperations.getChildren(app, MetaAdapterFactory.getContainmentLink(0x6d84bad7eb044249L, 0x8d80c9157947c3f2L, 0x1ee64324e96f0fc1L, 0x1ee64324e96f0fd2L, "states"))).where(new IWhereFilter<SNode>() {
      public boolean accept(SNode s) {
        return eq_qpdiil_a0a0a0a0a0a0a0b(SPropertyOperations.getBoolean(s, MetaAdapterFactory.getProperty(0x6d84bad7eb044249L, 0x8d80c9157947c3f2L, 0x1ee64324e96f0fc4L, 0x1ee64324e970156aL, "isInitial")), true);
      }
    }).count() > 1) {
      {
        MessageTarget errorTarget = new NodeMessageTarget();
        IErrorReporter _reporter_2309309498 = typeCheckingContext.reportTypeError(app, "Duplicated initial state detected in" + SPropertyOperations.getString(app, MetaAdapterFactory.getProperty(0xceab519525ea4f22L, 0x9b92103b95ca8c0cL, 0x110396eaaa4L, 0x110396ec041L, "name")) + "!", "r:20dd5926-3136-454b-8579-6e02ae2c6fe9(ArduinoML.typesystem)", "2226540891595899522", null, errorTarget);
      }
    }
  }
  public SAbstractConcept getApplicableConcept() {
    return MetaAdapterFactory.getConcept(0x6d84bad7eb044249L, 0x8d80c9157947c3f2L, 0x1ee64324e96f0fc1L, "ArduinoML.structure.App");
  }
  public IsApplicableStatus isApplicableAndPattern(SNode argument) {
    return new IsApplicableStatus(argument.getConcept().isSubConceptOf(getApplicableConcept()), null);
  }
  public boolean overrides() {
    return false;
  }
  private static boolean eq_qpdiil_a0a0a0a0a0a0a0b(Object a, Object b) {
    return (a != null ? a.equals(b) : a == b);
  }
}
