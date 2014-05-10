package cr.ucr.biomsc.datamining.solr;

import org.apache.lucene.analysis.Tokenizer;
import org.openscience.cdk.interfaces.IAtomContainer;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by equiros on 5/7/14.
 */
public class SmilesTokenizer extends Tokenizer {
  public static enum MODE {ATOMS, RINGS}
  private MODE mode;
  private IAtomContainer molecule;

  @Override
  public boolean incrementToken() throws IOException {
    return false;
  }

  public SmilesTokenizer(MODE mode, Reader input) {
    super(input);
    this.mode = mode;
    init(mode);
  }

  private void init(MODE mode) {

  }

  public SmilesTokenizer(AttributeFactory factory, Reader input, MODE mode) {
    super(factory, input);
    this.mode = mode;
    init(mode);
  }
}
