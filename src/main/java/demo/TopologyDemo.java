package demo;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
/**
 *  创建、提交拓扑
 * @author zhouwhui
 *
 */

public class TopologyDemo {

	// java程序执行入口
	public static void main(String[] args) throws Exception {

		// 实例化拓扑
		TopologyBuilder builder = new TopologyBuilder();
		// 设置发出句子集spout
		builder.setSpout("datasource-spout", new DataSourceSpout());
		//  随机分发
		builder.setBolt("split-bolt", new SplitBolt()).shuffleGrouping("datasource-spout");
		// 字段分发
		builder.setBolt("count-bolt", new WordCountBolt()).fieldsGrouping("split-bolt", new Fields("word"));
		// 全局分发
		builder.setBolt("display-bolt", new DisplayBolt()).globalGrouping("count-bolt");


		Config config = new Config();
		// 当前拓扑运行时 需要占用两个槽位（2个worker进程）
		config.setNumWorkers(2);
		config.setNumAckers(0); // storm可靠性处理相关
		StormSubmitter.submitTopology("word-count-Storm-topology",config,builder.createTopology());

		// 在虚拟机本地运行
//		if (args != null && args.length > 0) {
//			// 提交到集群模式
//			System.out.println("submitting on cluster mode");
//			StormSubmitter.submitTopology("word-count-Storm-topology", config, builder.createTopology());
//		} else {
//			// 提交到本地模式（单机模式）
//			System.out.println("submitting on local mode");
//			LocalCluster cluster = new LocalCluster();
//			// 提交拓扑
//			cluster.submitTopology("word-count-Storm-topology", config, builder.createTopology());
//
//			Thread.sleep(20000);
//			// 关闭拓扑
//			cluster.killTopology("word-count-Storm-topology");
//			cluster.shutdown();
//		}
	}
}
