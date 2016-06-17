package dna.konect.analysis;

import java.io.File;
import java.io.IOException;

import dna.graph.datastructures.GDS;
import dna.graph.datastructures.GraphDataStructure;
import dna.graph.generators.GraphGenerator;
import dna.graph.generators.konect.KonectGraph;
import dna.graph.generators.konect.KonectReader;
import dna.graph.generators.konect.KonectReader.KonectBatchType;
import dna.graph.generators.konect.KonectReader.KonectEdgeType;
import dna.graph.generators.konect.KonectReader.KonectGraphType;
import dna.graph.weights.Weight;
import dna.graph.weights.Weight.WeightSelection;
import dna.metrics.Metric;
import dna.metrics.MetricNotApplicableException;
import dna.metrics.assortativity.AssortativityR;
import dna.metrics.clustering.DirectedClusteringCoefficientR;
import dna.metrics.clustering.UndirectedClusteringCoefficientR;
import dna.metrics.degree.DegreeDistributionR;
import dna.metrics.motifs.DirectedMotifsR;
import dna.metrics.motifs.UndirectedMotifsR;
import dna.metrics.paths.unweighted.UnweightedAllPairsShortestPathsR;
import dna.metrics.weights.EdgeWeightsR;
import dna.metrics.weights.NodeWeightsR;
import dna.plot.Plotting;
import dna.plot.PlottingConfig;
import dna.plot.PlottingConfig.PlotFlag;
import dna.series.AggregationException;
import dna.series.Series;
import dna.series.data.SeriesData;
import dna.updates.generators.BatchGenerator;
import dna.updates.generators.konect.KonectBatch;
import dna.util.Config;

public class Analysis {

	public static void main(String[] args) throws IOException,
			AggregationException, MetricNotApplicableException,
			InterruptedException {
		// args = new String[] { "ANALYSIS_AND_PLOT", "",
		// "/Users/benni/TUD/datasets/dynamic/KonectNew/datasets/",
		// "contact", "U_E", "dna.graph.weights.IntWeight", "MULTI", "",
		// "PROCESSED_EDGES", "10", "PROCESSED_EDGES", "10", "2",
		// "DD-APSP-M_U" };
		// args = new String[] { "ANALYSIS_AND_PLOT", "",
		// "/Users/benni/TUD/datasets/dynamic/KonectNew/datasets/",
		// "sociopatterns-hypertext", "U_E",
		// "dna.graph.weights.IntWeight", "MULTI", "", "TIMESTAMP",
		// "1246255219", "TIMESTAMP", "600", "100000", "DD-W_E" };
		if (!check(args)) {
			printHelp(args);
			return;
		}
		Analysis a = new Analysis(args);
		a.exectute();
	}

	public static enum AnalysisType {
		ANALYSIS, PLOT, ANALYSIS_AND_PLOT
	}

	public static enum GdsType {
		D, U, D_E, D_V, U_E, U_V
	}

	public static enum Metrics {
		DD, W_E, W_V, APSP, M_D, M_U, ASS, CC_U, CC_D
	}

	public AnalysisType at;
	public String ap;

	public String path;
	public String name;

	public GdsType gdst;
	public String gdsp;

	public KonectEdgeType et;
	public String ep;

	public KonectGraphType gt;
	public String gp;

	public KonectBatchType bt;
	public String bp;

	public String metrics;

	public int batches;

	public Analysis(String[] args) {
		int index = 0;
		at = AnalysisType.valueOf(args[index++]);
		ap = args[index++];
		path = args[index++];
		name = args[index++];
		gdst = GdsType.valueOf(args[index++]);
		gdsp = args[index++];
		et = KonectEdgeType.valueOf(args[index++]);
		ep = args[index++];
		gt = KonectGraphType.valueOf(args[index++]);
		gp = args[index++];
		bt = KonectBatchType.valueOf(args[index++]);
		bp = args[index++];
		batches = Integer.parseInt(args[index++]);
		metrics = args[index++];
	}

	public void exectute() throws IOException, AggregationException,
			MetricNotApplicableException, InterruptedException {
		switch (at) {
		case ANALYSIS:
			Config.zipNone();
			analysis();
			break;
		case ANALYSIS_AND_PLOT:
			Config.zipNone();
			analysis();
			plot();
			break;
		case PLOT:
			Config.zipRuns();
			plot();
			break;
		default:
			throw new IllegalArgumentException("unknown AnalysisType " + at);
		}
	}

	public void analysis() throws AggregationException, IOException,
			MetricNotApplicableException, InterruptedException {

		if ((new File(getDataDir())).exists()) {
			System.out.println(getDataDir() + " exists already");
			return;
		}

		Config.overwrite("GENERATE_VALUES_FROM_DISTRIBUTION", "false");
		Config.overwrite("GENERATE_VALUES_FROM_NODEVALUELIST", "false");

		KonectReader r = new KonectReader(getSrcDir(), getSrcFilename(), name,
				getGds(), et, ep);
		GraphGenerator gg = new KonectGraph(r, gt, gp);
		BatchGenerator bg = new KonectBatch(r, bt, bp);
		Metric[] metrics = getMetrics();

		Series s = new Series(gg, bg, metrics, getDataDir(), name);
		s.generateRuns(0, 0, batches);
	}

	public void plot() throws IOException, InterruptedException {
		SeriesData sd = SeriesData.read(getDataDir(), name, false, true);
		plot(sd);
	}

	public void plot(SeriesData... sd) throws IOException, InterruptedException {
		if ((new File(getPlotDir())).exists()) {
			System.out.println(getPlotDir() + " exists already");
			return;
		}
		if (this.bt.equals(KonectBatchType.TIMESTAMP)
				|| this.bt.equals(KonectBatchType.TIMESTAMPS)) {
			Config.overwrite("GNUPLOT_PLOTDATETIME", "true");
			Config.overwrite("GNUPLOT_DATETIME", "%Y\\n%m-%d\\n%H:%m");
		}
		PlottingConfig cfg = new PlottingConfig(
				PlotFlag.plotSingleScalarValues, PlotFlag.plotStatistics);
		Plotting.plotRun(sd, 0, getPlotDir(), cfg);
	}

	public String getSrcDir() {
		return path + et + "/" + name + "/";
	}

	public String getSrcFilename() {
		return "sorted." + name;
	}

	public String getDataDir() {
		return getSrcDir() + "_" + toString() + "_data/";
	}

	public String getPlotDir() {
		return getSrcDir() + "_" + toString() + "_plots/";
	}

	public String toString() {
		return gdst + "--" + gdsp + "--" + et + "--" + ep + "--" + gt + "--"
				+ gp + "--" + bt + "--" + bp + "--" + metrics + "--" + batches;
	}

	@SuppressWarnings("unchecked")
	public GraphDataStructure getGds() {
		try {
			switch (gdst) {
			case D:
				return GDS.directed();
			case D_E:
				return GDS.directedE(
						(Class<? extends Weight>) Class.forName(gdsp),
						WeightSelection.Zero);
			case D_V:
				return GDS.directedV(
						(Class<? extends Weight>) Class.forName(gdsp),
						WeightSelection.Zero);
			case U:
				return GDS.undirected();
			case U_E:
				return GDS.undirectedE(
						(Class<? extends Weight>) Class.forName(gdsp),
						WeightSelection.Zero);
			case U_V:
				return GDS.undirectedV(
						(Class<? extends Weight>) Class.forName(gdsp),
						WeightSelection.Zero);
			default:
				throw new IllegalArgumentException("unknown GdsType " + gdst);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Metric[] getMetrics() {
		String[] m = this.metrics.split("-");
		Metric[] metrics = new Metric[m.length];
		for (int i = 0; i < m.length; i++) {
			metrics[i] = getMetric(Metrics.valueOf(m[i]));
		}
		return metrics;
	}

	public static Metric getMetric(Metrics m) {
		switch (m) {
		case DD:
			return new DegreeDistributionR();
		case APSP:
			return new UnweightedAllPairsShortestPathsR();
		case M_D:
			return new DirectedMotifsR();
		case M_U:
			return new UndirectedMotifsR();
		case W_E:
			return new EdgeWeightsR(1.0);
		case W_V:
			return new NodeWeightsR(1.0);
		case ASS:
			return new AssortativityR();
		case CC_D:
			return new DirectedClusteringCoefficientR();
		case CC_U:
			return new UndirectedClusteringCoefficientR();
		default:
			throw new IllegalArgumentException("unknown metric type: " + m);
		}
	}

	public static boolean check(String[] args) {
		return args.length == 14;
	}

	public static void printHelp(String[] args) {
		System.err.println("expecting 14 parameters (" + args.length
				+ " given)");
		System.err.println("   0: at - " + toString(AnalysisType.values()));
		System.err.println("   1: ap");
		System.err.println("   2: path to datasets");
		System.err.println("   3: name of dataset");
		System.err.println("   4: gdst - " + toString(GdsType.values()));
		System.err.println("   5: gdsp");
		System.err.println("   6: et - " + toString(KonectEdgeType.values()));
		System.err.println("   7: ep");
		System.err.println("   8: gt - " + toString(KonectGraphType.values()));
		System.err.println("   9: gp");
		System.err.println("  10: bt - " + toString(KonectBatchType.values()));
		System.err.println("  11: bp");
		System.err.println("  12: batches");
		System.err.println("  13: metrics sep. by '-' - "
				+ toString(Metrics.values()));
	}

	public static String toString(Object[] values) {
		StringBuffer buff = new StringBuffer();
		for (Object value : values) {
			if (buff.length() == 0) {
				buff.append(value.toString());
			} else {
				buff.append(", " + value.toString());
			}
		}
		return buff.toString();
	}
}
