package ArduinoML.editor;

/*Generated by MPS */

import jetbrains.mps.nodeEditor.menus.substitute.SubstituteMenuBase;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import jetbrains.mps.lang.editor.menus.MenuPart;
import jetbrains.mps.openapi.editor.menus.substitute.SubstituteMenuItem;
import jetbrains.mps.openapi.editor.menus.substitute.SubstituteMenuContext;
import java.util.ArrayList;
import jetbrains.mps.lang.editor.menus.substitute.ConstraintsFilteringSubstituteMenuPartDecorator;
import jetbrains.mps.smodel.adapter.structure.MetaAdapterFactory;
import jetbrains.mps.lang.editor.menus.EditorMenuDescriptorBase;
import jetbrains.mps.smodel.SNodePointer;
import jetbrains.mps.lang.editor.menus.substitute.ReferenceScopeSubstituteMenuPart;

public class Action_SubstituteMenu extends SubstituteMenuBase {
  @NotNull
  @Override
  protected List<MenuPart<SubstituteMenuItem, SubstituteMenuContext>> getParts(final SubstituteMenuContext _context) {
    List<MenuPart<SubstituteMenuItem, SubstituteMenuContext>> result = new ArrayList<MenuPart<SubstituteMenuItem, SubstituteMenuContext>>();
    result.add(new ConstraintsFilteringSubstituteMenuPartDecorator(new Action_SubstituteMenu.SMP_ReferenceScope_g3c1ep_a(), MetaAdapterFactory.getConcept(0x6d84bad7eb044249L, 0x8d80c9157947c3f2L, 0x1ee64324e96f0fc7L, "ArduinoML.structure.Action")));
    return result;
  }

  @NotNull
  @Override
  public List<SubstituteMenuItem> createMenuItems(@NotNull SubstituteMenuContext context) {
    context.getEditorMenuTrace().pushTraceInfo();
    context.getEditorMenuTrace().setDescriptor(new EditorMenuDescriptorBase("default substitute menu for Action. Generated from implicit smart reference attribute.", new SNodePointer("r:95dc3936-047b-4df0-883d-ba757a2402dd(ArduinoML.structure)", "2226540891595542471")));
    try {
      return super.createMenuItems(context);
    } finally {
      context.getEditorMenuTrace().popTraceInfo();
    }
  }


  public static class SMP_ReferenceScope_g3c1ep_a extends ReferenceScopeSubstituteMenuPart {

    public SMP_ReferenceScope_g3c1ep_a() {
      super(MetaAdapterFactory.getConcept(0x6d84bad7eb044249L, 0x8d80c9157947c3f2L, 0x1ee64324e96f0fc7L, "ArduinoML.structure.Action"), MetaAdapterFactory.getReferenceLink(0x6d84bad7eb044249L, 0x8d80c9157947c3f2L, 0x1ee64324e96f0fc7L, 0x1ee64324e96f0fe2L, "target"));
    }
    @NotNull
    @Override
    public List<SubstituteMenuItem> createItems(SubstituteMenuContext context) {
      context.getEditorMenuTrace().pushTraceInfo();
      context.getEditorMenuTrace().setDescriptor(new EditorMenuDescriptorBase("reference scope substitute menu part", null));
      try {
        return super.createItems(context);
      } finally {
        context.getEditorMenuTrace().popTraceInfo();
      }
    }

  }
}
