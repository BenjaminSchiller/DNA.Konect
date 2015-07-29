package dna.konect.test;

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
import dna.metrics.Metric;
import dna.metrics.degree.DegreeDistributionR;
import dna.metrics.weights.EdgeWeightsR;
import dna.plot.Plotting;
import dna.plot.PlottingConfig.PlotFlag;
import dna.series.Series;
import dna.series.data.SeriesData;
import dna.updates.batch.Batch;
import dna.updates.generators.BatchGenerator;
import dna.updates.generators.konect.KonectBatch;
import dna.util.Config;
import dna.util.Execute;
import dna.util.Log;

public class TestKonectReader {

	public static void main(String[] args) throws Exception {

		String[] rating = new String[] { "moreno_vdb", "movielens-100k_rating",
				"elec" };
		for (String name : rating) {
			// blub(name, GDS.directedE(IntWeight.class, WeightSelection.Zero),
			// KonectEdgeType.WEIGHTED, "1;1",
			// KonectGraphType.PROCESSED_EDGES, "100",
			// KonectBatchType.PROCESSED_EDGES, "100", 20, true);
		}

		String[] multi = new String[] { "contact", "edit-frwikibooks",
				"edit-frwikinews", "movielens-10m_ti", "movielens-10m_ui",
				"movielens-10m_ut", "munmun_digg_reply", "opsahl-ucforum",
				"opsahl-ucsocial", "radoslaw_email", "slashdot-threads",
				"sociopatterns-hypertext", "sociopatterns-infectious",
				"topology" };
		for (String name : multi) {
			// blub(name, GDS.directedE(IntWeight.class, WeightSelection.Zero),
			// KonectEdgeType.MULTI, "", KonectGraphType.PROCESSED_EDGES,
			// "100", KonectBatchType.PROCESSED_EDGES, "100", 20, true);
		}
		blub("aaa", GDS.directedE(IntWeight.class, WeightSelection.Zero),
				KonectEdgeType.MULTI, "30", KonectGraphType.TIMESTAMP, "50",
				KonectBatchType.TIMESTAMP, "10", 20, true);

		KonectGraphType gt = KonectGraphType.PROCESSED_EDGES;
		int gp = 10;
		KonectBatchType bt = KonectBatchType.PROCESSED_EDGES;
		int bp = 10;
		int batches = 1000;

		boolean generate = false;

		GraphDataStructure d_mu = GDS.directedE(IntWeight.class,
				WeightSelection.One);
		KonectEdgeType mu = KonectEdgeType.MULTI;

		GraphDataStructure d = GDS.directed();
		KonectEdgeType ar = KonectEdgeType.ADD_REMOVE;

		GraphDataStructure u = GDS.undirected();
		KonectEdgeType a = KonectEdgeType.ADD;

		// blub("radoslaw_email",
		// GDS.directedE(IntWeight.class, WeightSelection.One),
		// KonectEdgeType.MULTI_UNWEIGHTED,
		// KonectGraphType.PROCESSED_EDGES, "500",
		// KonectBatchType.PROCESSED_EDGES, "825", 200, generate);
		// blub("radoslaw_email",
		// GDS.directedE(IntWeight.class, WeightSelection.One),
		// KonectEdgeType.MULTI_UNWEIGHTED, KonectGraphType.TIMESTAMP,
		// "1264982400", KonectBatchType.TIMESTAMP, "" + (24 * 60 * 60),
		// 300, generate);
		// blub("radoslaw_email",
		// GDS.directedE(IntWeight.class, WeightSelection.One),
		// KonectEdgeType.MULTI_UNWEIGHTED, KonectGraphType.TIMESTAMP,
		// "1264982400", KonectBatchType.TIMESTAMP, "" + (24 * 60 * 60),
		// 7 * 8, generate);

		// 12368
		// blub("munmun_digg_reply",
		// GDS.directedE(IntWeight.class, WeightSelection.One),
		// KonectEdgeType.MULTI_UNWEIGHTED,
		// KonectGraphType.PROCESSED_EDGES, "500",
		// KonectBatchType.PROCESSED_EDGES, "872", 200, generate);
		// blub("munmun_digg_reply",
		// GDS.directedE(IntWeight.class, WeightSelection.One),
		// KonectEdgeType.MULTI_UNWEIGHTED, KonectGraphType.TIMESTAMP,
		// "1225229529", KonectBatchType.TIMESTAMP, "12368", 200, generate);

		// munmun_digg_reply
		// slashdot-threads

		// 927846000 - 913330800 = 14515200
		// blub("moreno_vdb",
		// GDS.directedE(IntWeight.class, WeightSelection.Zero),
		// KonectEdgeType.RATING_ADD_ONE, KonectGraphType.TIMESTAMP,
		// "913330800", KonectBatchType.TIMESTAMP, "145152", 1000,
		// generate);
		// blub("moreno_vdb",
		// GDS.directedE(IntWeight.class, WeightSelection.Zero),
		// KonectEdgeType.RATING_ADD_ONE, KonectGraphType.PROCESSED_EDGES,
		// "100", KonectBatchType.PROCESSED_EDGES, "100", 1000, generate);
		// blub("moreno_vdb",
		// GDS.directedE(IntWeight.class, WeightSelection.Zero),
		// KonectEdgeType.RATING_ADD_ONE, KonectGraphType.TIMESTAMP,
		// "913330800", KonectBatchType.TIMESTAMPS,
		// "915145200;916959600;918774000;920588400;924217200;927846000",
		// 1000, generate);

		// 893286638 - 874724710 = 18561928
		// blub("movielens-100k_rating",
		// GDS.directedE(IntWeight.class, WeightSelection.Zero),
		// KonectEdgeType.RATING, KonectGraphType.TIMESTAMP, "874724710",
		// KonectBatchType.TIMESTAMP, "185619", 200, generate);
		// blub("movielens-100k_rating",
		// GDS.directedE(IntWeight.class, WeightSelection.Zero),
		// KonectEdgeType.RATING, KonectGraphType.PROCESSED_EDGES, "500",
		// KonectBatchType.PROCESSED_EDGES, "500", 200, generate);

		System.out.println("\n\nDONE");
	}

	public static void blub(String name, GraphDataStructure gds,
			KonectEdgeType et, String ep, KonectGraphType gt, String gp,
			KonectBatchType bt, String bp, int batches, boolean generate)
			throws Exception {

		String dir = "/Users/benni/TUD/datasets/dynamic/KonectNew/datasets/"
				+ et + "/" + name + "/";
		String filename = "sorted." + name;

		KonectReader r = new KonectReader(dir, filename, name, gds, et, ep);
		GraphGenerator gg = new KonectGraph(r, gt, gp);
		BatchGenerator bg = new KonectBatch(r, bt, bp);

		if (generate) {
			generate(name, et, gg, bg, batches);
		} else {
			String dst = "/Users/benni/TUD/datasets/dynamic/KonectNew/datasets/"
					+ et
					+ "/"
					+ name
					+ "/_"
					+ gt
					+ "-"
					+ gp
					+ "-"
					+ bt
					+ "-"
					+ bp + "-" + batches + "/";
			analyze(name, dst, et, gg, bg, batches);
		}
	}

	public static void generate(String name, KonectEdgeType et,
			GraphGenerator gg, BatchGenerator bg, int batches) throws Exception {

		Config.overwrite("GRAPH_VIS_WAIT_ENABLED", "true");
		// GraphVisualization.disable();

		Log.infoSep();
		Log.info("generating " + name);
		Log.infoSep();

		dna.graph.Graph g = gg.generate();
		// GraphVisualization
		// .getCurrentGraphPanel()
		// .getGraph()
		// .addAttribute(GraphVisualization.screenshotsKey,
		// "images/screenshot" + g.getTimestamp() + ".png");
		System.out.println(g);
		for (int i = 0; i < batches; i++) {
			if (!bg.isFurtherBatchPossible(g))
				break;
			Batch b = bg.generate(g);
			System.out.println(b);
			b.apply(g);
			// Thread.sleep(500);
			// GraphVisualization
			// .getCurrentGraphPanel()
			// .getGraph()
			// .addAttribute(GraphVisualization.screenshotsKey,
			// "images/screenshot" + g.getTimestamp() + ".png");
		}

		Log.infoSep();
	}

	public static void analyze(String name, String dir, KonectEdgeType et,
			GraphGenerator gg, BatchGenerator bg, int batches) throws Exception {

		// GraphVisualization.disable();
		Config.zipRuns();
		Config.overwrite("GENERATE_VALUES_FROM_DISTRIBUTION", "false");
		Config.overwrite("GENERATE_VALUES_FROM_NODEVALUELIST", "false");

		Log.infoSep();
		Log.info("generating " + name);
		Log.infoSep();

		String data = dir + "_data/";
		Execute.exec("rm -r " + data);
		String plots = dir + "_plots/";
		Execute.exec("rm -r " + plots);

		Series s = new Series(gg, bg, getMetrics(et), data, name);
		SeriesData sd = s.generate(1, batches);
		Plotting.plot(sd, plots, PlotFlag.plotStatistics,
				PlotFlag.plotMetricValues);

		Log.infoSep();
	}

	public static Metric[] getMetrics(KonectEdgeType et) {
		switch (et) {
		case ADD:
			return new Metric[] { new DegreeDistributionR() };
		case ADD_REMOVE:
			return new Metric[] { new DegreeDistributionR() };
		case MULTI:
			return new Metric[] { new DegreeDistributionR(),
					new EdgeWeightsR(1) };
		case WEIGHTED:
			return new Metric[] { new DegreeDistributionR(),
					new EdgeWeightsR(1) };
		default:
			return null;
		}
	}
}
