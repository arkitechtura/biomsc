package cr.ucr.biomsc.datamining.cdk;

import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

/**
 * Created by equiros on 03/05/14.
 */
public class SmilesParserTest {
  public static void main(String[] args) {
    String smiles = "c1ccccc1";
    IAtomContainer molecule;
    try {
      SmilesParser parser = new SmilesParser(SilentChemObjectBuilder.getInstance());
      molecule = parser.parseSmiles(smiles);
      System.out.println("Done!");
    } catch (InvalidSmilesException e) {
      e.printStackTrace();
    }
  }
}
