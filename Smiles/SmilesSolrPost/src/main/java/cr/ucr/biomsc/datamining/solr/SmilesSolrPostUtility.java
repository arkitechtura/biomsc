package cr.ucr.biomsc.datamining.solr;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

/**
 * Created by equiros on 5/14/2014.
 */
public class SmilesSolrPostUtility {
  public static Logger logger = LoggerFactory.getLogger(SmilesSolrPostUtility.class);

  private final String fileName;

  SmilesSolrPostUtility(String fileName) {
    this.fileName = fileName;
  }

  void process() {
    try {
      CSVReader reader = initializeReader();
      HttpSolrServer solr = initializeSolr();
      processFile(reader, solr);
    }
    catch (Exception ex) {
      logger.error("Error processing file '" + fileName + "'", ex);
    }
  }

  class CSVReaderIterator implements Iterator<SmilesDocument> {
    private final CSVReader reader;
    private String[] line;
    private SmilesGenerator generator;
    private SmilesParser parser;

    CSVReaderIterator(CSVReader reader) {
      this.reader = reader;
      generator = new SmilesGenerator();
      parser = new SmilesParser(SilentChemObjectBuilder.getInstance());
    }

    @Override
    public boolean hasNext() {
      try {
        line = reader.readNext();
        return line != null;
      }
      catch (IOException e) {
        logger.error("Error reading from CSVReader", e);
        throw new RuntimeException(e);
      }
    }

    @Override
    public SmilesDocument next() {
      return buildDocument(line);
    }

    private SmilesDocument buildDocument(String[] line) {
      try {
        SmilesDocument doc = new SmilesDocument();
        doc.pubchemid = Integer.parseInt(line[0]);
        doc.smiles = line[1];
        doc.toxicity = Integer.parseInt(line[2]);
        doc.canonicalSmiles = canonicalizeSmiles(doc.smiles);
        return doc;
      }
      catch (InvalidSmilesException e) {
        logger.error("Error creating SmilesDocument", e);
        throw new RuntimeException(e);
      }
    }

    private String canonicalizeSmiles(String smiles) throws InvalidSmilesException {
      return generator.createSMILES(parser.parseSmiles(smiles));
    }

    @Override
    public void remove() {

    }
  }

  private void processFile(final CSVReader reader, HttpSolrServer solr) throws IOException, SolrServerException {
    Iterator<SmilesDocument> iterator = new CSVReaderIterator(reader);
    solr.addBeans(iterator);
    solr.commit();
  }

  private HttpSolrServer initializeSolr() throws IOException {
    Properties props = new Properties();
    InputStream is = getClass().getResourceAsStream("/default.properties");
    if (is != null) props.load(is);
    String url = "http://localhost:8983/solr";
    if (props.containsKey("solr.url"))
      url = props.getProperty("solr.url");
    return new HttpSolrServer(url);
  }

  public static void main(String[] args) {
    SmilesSolrPostUtility utility = new SmilesSolrPostUtility(args[0]);
    utility.process();
  }

  public CSVReader initializeReader() throws IOException {
    logger.info("Attempting to initialize CSV Reader for file '" + fileName + "'");
    CSVReader r = new CSVReader(new BufferedReader(new FileReader(fileName)));
    return r;
  }
}
