#!/bin/bash

function create {
	dir="$3$7/$4/_$5--$6--$7--$8--$9--${10}--${11}--${12}--${14}--${13}_data/"
	./jobs.sh create "java -jar -Xms256m -Xmx4g analysis.jar $1 $2 $3 $4 $5 $6 $7 $8 $9 ${10} ${11} ${12} ${13} ${14}; cd ${dir}run.0/; zip -r ../run.0.zip .; cd ..; rm -r run.0/"
}


# expecting 14 parameters (0 given)
#    0: at - ANALYSIS, PLOT, ANALYSIS_AND_PLOT
#    1: ap
#    2: path to datasets
#    3: name of dataset
#    4: gdst - D, U, D_E, D_V, U_E, U_V
#    5: gdsp
#    6: et - ADD, ADD_REMOVE, MULTI, WEIGHTED
#    7: ep
#    8: gt - PROCESSED_EDGES, TIMESTAMP, TOTAL_EDGES, TOTAL_NODES
#    9: gp
#   10: bt - BATCH_SIZE, EDGE_GROWTH, NODE_GROWTH, PROCESSED_EDGES, TIMESTAMP, TIMESTAMPS
#   11: bp
#   12: batches
#   13: metrics sep. by '-' - DD, W_E, W_V, APSP, M_D, M_U

function multiProcessed {
	# 1: name
	# 2: total lines in file
	bp=$(echo "$2/100+1" | bc)
	create ANALYSIS_AND_PLOT - ../datasets/ $1 U_E dna.graph.weights.IntWeight MULTI - PROCESSED_EDGES 0 PROCESSED_EDGES $bp 999999 DD-W_E
}

function multiTimestamp {
	# 1: name
	# 2: B/D/U for gds
	# 3: first timestamp
	# 4: last timestamp
	name=$1
	gp=$(echo "$3-1" | bc)
	bp=$(echo "($4-$3)/100+1" | bc)
	if [[ $2 = "B" ]]; then gds="D_E"
	elif [[ $2 = "D" ]]; then gds="D_E"
	elif [[ $2 = "U" ]]; then gds="U_E"
	else echo "invalid gds parameter: $2"; exit
	fi
	create ANALYSIS_AND_PLOT - ../datasets/ $name $gds dna.graph.weights.IntWeight MULTI - TIMESTAMP $gp TIMESTAMP $bp 999999 DD-W_E-ASS
}

# multiTimestamp sociopatterns-infectious	U	1247652139	1247680559
# multiTimestamp sociopatterns-hypertext	U	1246255220	1246467560
# multiTimestamp contact	U	20733	364094
# multiTimestamp opsahl-ucforum	B	1084560796	1098772901
# multiTimestamp opsahl-ucsocial	D	1082008561	1098744742
# multiTimestamp radoslaw_email	D	1262454010	1285884492
# multiTimestamp munmun_digg_reply	D	1225229529	1226536024
# multiTimestamp movielens-10m_ti	B	1135313387	1231128790
# multiTimestamp movielens-10m_ui	B	1135313387	1231128790
# multiTimestamp movielens-10m_ut	B	1135313387	1231128790
# multiTimestamp slashdot-threads	D	0	1157361660
# multiTimestamp topology	U	1266192000	1268208004


multiTimestamp edit-frwikinews	B	1070606520	1283257812
multiTimestamp edit-frwikibooks	B	1014452530	1283235484
multiTimestamp edit-enwikiquote	B	1041639294	1283191468
multiTimestamp edit-enwikinews	B	1099901502	1283218245
multiTimestamp mit	U	1095183096	1115253696
multiTimestamp facebook-wosn-wall	D	1095135831	1232598691
multiTimestamp lkml-reply	D	1136080607	1388528616
multiTimestamp enron	D	315532800	1716720597
multiTimestamp edit-dewiktionary	B	1013645942	1283522716
multiTimestamp edit-enwikibooks	B	1005798855	1283031674
multiTimestamp lkml_person-thread	B	315782782	1399453372
multiTimestamp bibsonomy-2ut	B	599996400	1262289689
multiTimestamp bibsonomy-2ui	B	599996400	1262289689
multiTimestamp bibsonomy-2ti	B	599996400	1262289689
multiTimestamp citeulike-ut	B	1099535105	1203482671
multiTimestamp citeulike-ti	B	1099535105	1203482671
multiTimestamp citeulike-ui	B	1099535105	1203482671
multiTimestamp prosper-loans	D	1132012800	1317081600
multiTimestamp edit-frwiktionary	B	1032721456	1283702846
multiTimestamp edit-enwiktionary	B	987260484	1283377300
multiTimestamp lastfm_band	B	1108339207	1380479524
multiTimestamp lastfm_song	B	1108339207	1380479524
multiTimestamp dblp_coauthor	U	-1.009.843.139	1388534461
multiTimestamp edit-itwiki	B	1041638615	1283475084
multiTimestamp edit-eswiki	B	1017217136	1283116655
multiTimestamp edit-frwiki	B	1036059361	1283302334
multiTimestamp edit-dewiki	B	986236061	1283594871
multiTimestamp edit-enwiki	B	979768389	1283771188
multiTimestamp delicious-ut	B	1062367200	1199055600
multiTimestamp delicious-ti	B	1062367200	1199055600
multiTimestamp delicious-ui	B	1062367200	1199055600
