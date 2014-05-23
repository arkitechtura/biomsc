package cr.ucr.biomsc.datamining.solr;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.smiles.SmilesGenerator;

/**
 * Created by equiros on 5/13/2014.
 */
public class SmilesUtilities {
  public static String toAtomString(IAtomContainer container) {
    StringBuffer buff = new StringBuffer(container.getAtomCount());
    for (int i = 0; i < container.getAtomCount(); i++) {
      buff.append(container.getAtom(i).getSymbol());
    }
    return buff.toString();
  }
  private static SmilesGenerator generator = new SmilesGenerator();
  public static String toSmiles(IAtomContainer container) {
    return generator.createSMILES(container);
  }
}
