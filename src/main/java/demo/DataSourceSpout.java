package demo;

import java.util.Map;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
/**
 *  负责生产一行行的英文短语 的 spout
 * @author zhouwhui
 *
 */
public class DataSourceSpout extends BaseRichSpout {

	private SpoutOutputCollector collector;

	// 设置句子集（数据源）
	private String[] datas = { "This is example of chapter 4", "This is word count example", "Very basic example of Apache Storm",
			"Apache Storm is open source real time processing engine"};
	// 定义索引下标
	private int index = 0;

	public void open(@SuppressWarnings("rawtypes") Map config, TopologyContext context, SpoutOutputCollector collector) {
		// 实例化输出类
		this.collector = collector;
	}

	public void nextTuple() {
		// 获取数据
		String sentence = datas[index];
		System.out.println(sentence);
		// 将数据输出到数据流中
		this.collector.emit(new Values(sentence));
		// 当获取到最后一条数据时返回第一条数据重新获取
		index++;
		if (index >= datas.length) {
			index = 0;
		}
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// 说明输出字段类别为 datasource
		declarer.declare(new Fields("datasource"));
	}
}