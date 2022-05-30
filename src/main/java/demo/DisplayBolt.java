package demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
/**
 * 展示数据（打印控制台）
 * @author zhouwhui
 *
 */
public class DisplayBolt extends BaseRichBolt {

	private HashMap<String, Long> counts = null;

	public void prepare(@SuppressWarnings("rawtypes") Map config, TopologyContext context, OutputCollector collector) {
		this.counts = new HashMap<String, Long>();
	}

	public void execute(Tuple tuple) {
		String word = tuple.getStringByField("word");
		Long count = tuple.getLongByField("count");
		// 获取数据流中的键值对，并将键值对存放到一个map集合中
		this.counts.put(word, count);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// 当前bolt是用于获取最终结果并最终处理的，不需要再将该结果输出到数据流，所以不需要实现该方法
	}

	public void cleanup() {
		System.out.println("--- FINAL COUNTS ---");
		List<String> keys = new ArrayList<String>();
		// 获取map集合中的所有key值（即获取所有单词）
		keys.addAll(this.counts.keySet());
		// 将单词进行排序
		Collections.sort(keys);
		// 根据排序的单词逐个获取每个单词出现的次数，将结果输出到控制台
		for (String key : keys) {
			System.out.println(key + " : " + this.counts.get(key));
		}
		System.out.println("--------------");
	}
}