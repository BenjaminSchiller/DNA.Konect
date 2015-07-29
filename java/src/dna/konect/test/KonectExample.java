package dna.konect.test;

import java.io.FileNotFoundException;

import dna.graph.Graph;
import dna.graph.datastructures.GDS;
import dna.graph.datastructures.GraphDataStructure;
import dna.graph.generators.GraphGenerator;
import dna.graph.generators.konect.KonectGraph;
import dna.graph.generators.konect.KonectReader;
import dna.graph.generators.konect.KonectReader.KonectBatchType;
import dna.graph.generators.konect.KonectReader.KonectEdgeType;
import dna.graph.generators.konect.KonectReader.KonectGraphType;
import dna.graph.weights.IntWeight;
import dna.graph.weights.Weight.WeightSelection;
import dna.updates.generators.BatchGenerator;
import dna.updates.generators.konect.KonectBatch;

public class KonectExample {

	public static void main(String[] args) throws FileNotFoundException {
		testAdd();
	}

	public static void testAdd() throws FileNotFoundException {
		String name = "facebook-wosn-links";
		String dir = "../datasets/ADD/" + name + "/";
		String filename = "sorted." + name;
		GraphDataStructure gds = GDS.undirected();
		KonectEdgeType edgeType = KonectEdgeType.ADD;
		String edgeParameter = "";
		boolean removeZeroDegreeNodes = true;
		KonectReader r = new KonectReader(dir, filename, name, gds, edgeType,
				edgeParameter, removeZeroDegreeNodes);

		KonectGraphType graphType = KonectGraphType.PROCESSED_EDGES;
		String graphParameter = "100";
		GraphGenerator gg = new KonectGraph(r, graphType, graphParameter);

		KonectBatchType batchType = KonectBatchType.PROCESSED_EDGES;
		String batchParameter = "500";
		BatchGenerator bg = new KonectBatch(r, batchType, batchParameter);

		Graph g = gg.generate();
		System.out.println(g);
		System.out.println(bg.generate(g));
	}

	public static void testMulti() throws FileNotFoundException {
		String name = "sociopatterns-infectious";
		String dir = "../datasets/MULTI/" + name + "/";
		String filename = "sorted." + name;
		GraphDataStructure gds = GDS.undirectedE(IntWeight.class,
				WeightSelection.One);
		KonectEdgeType edgeType = KonectEdgeType.MULTI;
		String edgeParameter = "";
		boolean removeZeroDegreeNodes = true;
		KonectReader r = new KonectReader(dir, filename, name, gds, edgeType,
				edgeParameter, removeZeroDegreeNodes);

		KonectGraphType graphType = KonectGraphType.PROCESSED_EDGES;
		String graphParameter = "100";
		GraphGenerator gg = new KonectGraph(r, graphType, graphParameter);

		KonectBatchType batchType = KonectBatchType.PROCESSED_EDGES;
		String batchParameter = "500";
		BatchGenerator bg = new KonectBatch(r, batchType, batchParameter);

		Graph g = gg.generate();
		System.out.println(g);
		System.out.println(bg.generate(g));
	}
}
