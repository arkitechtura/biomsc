package cr.ucr.biomsc.datamining.solr;

import org.openscience.smsd.Substructure;

/**
 * Created by equiros on 7/3/2014.
 */
public class TanimotoDistance extends MolecularDistance {
  @Override
  protected double getMolecularDistance(Substructure substructure) {
    return substructure.getTanimotoSimilarity();
  }

  @Override
  protected double getDistanceOnMoleculeError() {
    return 0;
  }
}
