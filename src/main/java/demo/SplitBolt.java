package demo;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * 分词 bolt（根据空格）
 * @author zhouwhui
 *
 */
public class SplitBolt extends BaseRichBolt {

	private OutputCollector collector;

	public void prepare(@SuppressWarnings("rawtypes") Map config, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	public void execute(Tuple tuple) {
		// 从数据流中获取类别为 datasource 的数据
		String sentence = tuple.getStringByField("datasource");
		// 英文短句通过空格分割获取到单词
		String[] words = sentence.split(" ");
		for (String word : words) {
			// 将单词传输到数据流中
			this.collector.emit(new Values(word));
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// 指定输出的单词字段类别为 word
		declarer.declare(new Fields("word"));
	}
}