package cr.ucr.biomsc.datamining.solr;

import org.apache.solr.client.solrj.beans.Field;

/**
 * Created by equiros on 5/14/2014.
 */
public class SmilesDocument {
  @Field
  public String id;
  @Field("pubchem_id")
  public String cid;
  @Field("molecular_formula")
  public String molecularFormula;
  @Field("molecular_weight")
  public float molecularWeight;
  @Field("canonical_smiles")
  public String canonicalSmiles;
  @Field("isomeric_smiles")
  public String isomericSmiles;
  @Field
  public String name;
  @Field("exact_mass" )
  public float exactMass;
  @Field("heavy_atom_count")
  public int heavyAtomCount;
  @Field
  public int charge;
  @Field("volume_3d")
  public float volume3d;
  @Field("feature_ring_count_3d")
  public int featureRingCount3d;
  @Field("feature_hydrophobe_count_3d")
  public int featureHydrophobeCount3d;
  @Field
  public int toxicity;
}
