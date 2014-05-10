package cr.ucr.biomsc.datamining.solr;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeSource;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by equiros on 5/7/14.
 */
public class SmilesTokenizerFactory extends TokenizerFactory {
  private String mode = "atoms";
  private static final List<String> allowedModes = new ArrayList<String>() {
    {
      add("atoms");
      add("rings");
    }
  };

  public SmilesTokenizerFactory(Map<String, String> args) {
    super(args);
    mode = require(args, "tokenizerMode", allowedModes, false);
    if (!args.isEmpty()) {
      throw new IllegalArgumentException("Unknown parameters: " + args);
    }
  }

  @Override
  public Tokenizer create(AttributeSource.AttributeFactory attributeFactory, Reader reader) {
    SmilesTokenizer.MODE tokenizerMode = "atoms".equalsIgnoreCase(mode) ? SmilesTokenizer.MODE.ATOMS : SmilesTokenizer.MODE.RINGS;
    return new SmilesTokenizer(attributeFactory, reader, tokenizerMode);
  }
}
