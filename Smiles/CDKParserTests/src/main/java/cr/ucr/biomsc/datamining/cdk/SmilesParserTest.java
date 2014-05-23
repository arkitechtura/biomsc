package cr.ucr.biomsc.datamining.cdk;

import org.openscience.cdk.Molecule;
import org.openscience.cdk.MoleculeSet;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.interfaces.IRingSet;
import org.openscience.cdk.io.SMILESReader;
import org.openscience.cdk.ringsearch.SSSRFinder;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

import java.io.Reader;
import java.io.StringReader;

import static org.openscience.cdk.graph.ConnectivityChecker.*;

/**
 * Created by equiros on 03/05/14.
 */
public class SmilesParserTest {
  public static void main(String[] args) {
    String smiles = "COC(=O)C(\\C)=C\\C1C(C)(C)[C@H]1C(=O)O[C@@H]2C(C)=C(C(=O)C2)CC=CC=C";
    testOne(smiles);
    testTwo(smiles);
  }

  private static void testTwo(String smiles) {
    int totalRings = 0;
    try {
      Reader r = new StringReader(smiles);
      SMILESReader smilesReader = new SMILESReader(r);
      MoleculeSet components = new MoleculeSet();
      components = smilesReader.read(components);
      for (IAtomContainer component : components.atomContainers()) {
        IRingSet ringset = new SSSRFinder(component).findSSSR();
        totalRings += ringset.getAtomContainerCount();
      }
      System.out.println("Total Rings: " + totalRings);
    }
    catch (CDKException e) {
      e.printStackTrace();
    }
  }

  private static void testOne(String smiles) {
    IAtomContainer molecule;
    int totalRings = 0;
    try {
      SmilesParser parser = new SmilesParser(SilentChemObjectBuilder.getInstance());
      molecule = parser.parseSmiles(smiles);
      IMoleculeSet components = partitionIntoMolecules(molecule);
      for (IAtomContainer component : components.atomContainers()) {
        IRingSet ringset = new SSSRFinder(component).findSSSR();
        totalRings += ringset.getAtomContainerCount();
      }
      System.out.println("Total Rings: " + totalRings);
    } catch (InvalidSmilesException e) {
      e.printStackTrace();
    }
  }
}
