package cr.ucr.biomsc.datamining.solr;

import org.apache.lucene.search.spell.StringDistance;
import org.openscience.cdk.MoleculeSet;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.SMILESReader;
import org.openscience.smsd.Substructure;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by equiros on 7/3/2014.
 */
public abstract class MolecularDistance implements StringDistance {
  private static SMILESReader reader = new SMILESReader();

  private static IAtomContainer buildContainer(String s) throws CDKException, IOException {
    Reader r = new StringReader(s);
    reader.setReader(r);
    MoleculeSet components = new MoleculeSet();
    components = reader.read(components);
    if (components.getAtomContainerCount() == 0) {
      throw new RuntimeException("No atom containers were created. SMILES: " + s);
    }
    if (components.getAtomContainerCount() > 1) {
      throw new RuntimeException("Only single atom containers are supported. SMILES: " + s);
    }
    r.close();
    return components.getAtomContainer(0);
  }

  protected abstract double getMolecularDistance(Substructure substructure);

  protected abstract double getDistanceOnMoleculeError();

  @Override
  public float getDistance(String query, String target) {
    IAtomContainer q;
    IAtomContainer t;
    try {
      try {
        q = buildContainer(query);
        t = buildContainer(target);
      }
      catch (Exception e) {
        return (float) getDistanceOnMoleculeError();
      }
      Substructure comp = new Substructure(q, t, false, true, true, true);
      if (comp.isSubgraph()) {
        return (float) getMolecularDistance(comp);
      }
      return (float) 0.0;
    }
    catch (CDKException e) {
      throw new RuntimeException(e);
    }
  }
}
