package cr.ucr.biomsc.datamining.smsd;

import org.openscience.cdk.MoleculeSet;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.SMILESReader;
import org.openscience.smsd.Substructure;
import uk.ac.ebi.reactionblast.tools.ExtAtomContainerManipulator;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by equiros on 7/2/2014.
 */
public class Comparison {
    private static SMILESReader reader = new SMILESReader();

    private static IAtomContainer buildContainer(String s) throws CDKException, IOException {
        Reader r = new StringReader(s);
        reader.setReader(r);
        MoleculeSet components = new MoleculeSet();
        components = reader.read(components);
        if (components.getAtomContainerCount() == 0) {
            throw new RuntimeException("No atom containers were created");
        }
        if (components.getAtomContainerCount() > 1) {
            throw new RuntimeException("Only single atom containers are supported");
        }
        r.close();
        return components.getAtomContainer(0);
    }
    public static void main(String[] args) {
        try {
            IAtomContainer query = buildContainer("c1ccccc1");
            IAtomContainer target = buildContainer("c1ccc2ccccc2c1");
            //ExtAtomContainerManipulator.removeHydrogens(query);
            //ExtAtomContainerManipulator.removeHydrogens(target);
            Substructure comp = new Substructure(query, target, false, true, true, true);
            if (comp.isSubgraph()) {
                System.out.println("Tanimoto: " + comp.getTanimotoSimilarity());
                System.out.println("Euclidean: " + comp.getEuclideanDistance());
            }
        }
        catch (CDKException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
