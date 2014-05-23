package cr.ucr.biomsc.datamining.solr;

import org.apache.solr.client.solrj.beans.Field;

/**
 * Created by equiros on 5/14/2014.
 */
public class SmilesDocument {
  @Field("id")
  public String canonicalSmiles;
  @Field
  public String smiles;
  @Field
  public String name;
  @Field
  public int pubchemid;
  @Field("ch_formula")
  public String formula;
  @Field("ch_weight")
  public float weight;
  @Field
  public int toxicity;
  @Field
  public float hydrophobicity;
  @Field("polar_surface_area")
  public float polarSurfaceArea;
}
