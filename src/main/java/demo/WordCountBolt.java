package demo;

import java.util.HashMap;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * 计数 bolt
 * @author zhouwhui
 *
 */
public class WordCountBolt extends BaseRichBolt {

	private OutputCollector collector;
	private HashMap<String, Long> counts = null;

	public void prepare(@SuppressWarnings("rawtypes") Map config, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		// 实例化一个空的map集合用于存储（word，count）的键值对信息
		this.counts = new HashMap<String, Long>();
	}

	public void execute(Tuple tuple) {
		// 从数据流中取出单词
		String word = tuple.getStringByField("word");
		// 从map集合中获取该单词对应的键值对，如果单词不存在map中会返回null
		Long count = this.counts.get(word);
		// 如果word在map集合（counts）中是不存在，给该word一个初始count值为0
		if (count == null) {
			count = 0L;
		}
		// 在原本的count值上+1
		count++;
		this.counts.put(word, count);
		// 将键值对结果输出到数据流中
		this.collector.emit(new Values(word, count));
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// 由于本次输出到数据流的字段是两个字段，所以分别为第一个字段定义类型为word，第二个字段为count
		declarer.declare(new Fields("word", "count"));
	}
}