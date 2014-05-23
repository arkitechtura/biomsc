package cr.ucr.biomsc.datamining.solr;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.openscience.cdk.MoleculeSet;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IRing;
import org.openscience.cdk.interfaces.IRingSet;
import org.openscience.cdk.io.SMILESReader;
import org.openscience.cdk.ringsearch.SSSRFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by equiros on 5/7/14.
 */
public class SmilesTokenizer extends Tokenizer {
  public static enum MODE {ATOMS, RINGS}
  private MODE mode;
  SMILESReader reader = null;
  private IAtomContainer molecule;
  private IRingSet rings;
  int maxTokens;
  int current = 0;
  public static Logger log = LoggerFactory.getLogger(SmilesTokenizer.class);

  public SmilesTokenizer(MODE mode, AttributeFactory factory, Reader input) {
    super(factory, input);
    this.mode = mode;
    this.reader = new SMILESReader();
  }

  private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
  private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
  private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);

  @Override
  public boolean incrementToken() throws IOException {
    clearAttributes();
    log.debug("Current: " + current + ". Max: " + maxTokens);
    if (current < maxTokens) {
      termAtt.setEmpty();
      if (mode == MODE.ATOMS) {
        IAtom atom = molecule.getAtom(current);
        String symbol = atom.getSymbol();
        log.debug("About to return atom: " + symbol);
        termAtt.append(symbol);
        termAtt.setLength(symbol.length());
        offsetAtt.setOffset(current, current + symbol.length());
        //posIncrAtt.setPositionIncrement(1);
        typeAtt.setType("atom");
      }
      else if (mode == MODE.RINGS) {
        if (!(rings.getAtomContainer(current) instanceof IRing)) {
          throw new IOException("Error identifying next ring instance in Smiles Tokenizer. Container not assignable to IRing");
        }
        IRing ring = (IRing) rings.getAtomContainer(current);
        String ringString = SmilesUtilities.toSmiles(ring);
        log.debug("About to return ring: " + ringString);
        termAtt.append(ringString);
        termAtt.setLength(ringString.length());
        offsetAtt.setOffset(current, current + ringString.length());
        //posIncrAtt.setPositionIncrement(1);
        typeAtt.setType("ring");
      }
      current++;
      return true;
    }
    else {
      return false;
    }
  }

  private void internalInit(Reader r) throws Exception {
    log.info("Initializing SmilesTokenizer");
    this.reader.setReader(r);
    MoleculeSet components = new MoleculeSet();
    components = reader.read(components);
    log.info("Got " + components.getAtomContainerCount() + " atom containers");
    if (components.getAtomContainerCount() == 0) {
      maxTokens = 0;
      return;
    }
    if (components.getAtomContainerCount() > 1) {
      throw new Exception("Only single atom containers are supported");
    }
    log.debug("Components: " + components.getAtomContainer(0).toString());
    if (mode == MODE.ATOMS) {
      molecule = components.getAtomContainer(0);
      maxTokens = molecule.getAtomCount();
      log.info("Atoms. Max: " + maxTokens);
    }
    else {
      rings = new SSSRFinder(components.getAtomContainer(0)).findSSSR();
      maxTokens = rings.getAtomContainerCount();
      log.info("Rings. Max: " + maxTokens);
    }
    current = 0;
  }

  @Override
  public void reset() throws IOException {
    super.reset();
    log.info("Resetting SmilesTokenizer");
    try {
      internalInit(input);
    }
    catch (Exception e) {
      log.error("Error resetting SmilesTokenizer", e);
      throw new IOException("Error resetting SmilesTokenizer", e);
    }
  }

  @Override
  public void close() throws IOException {
    super.close();
    if (reader != null) reader.close();
  }
}
