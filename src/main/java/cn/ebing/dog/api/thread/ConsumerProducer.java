package cn.ebing.dog.api.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 我提到过 Queue 被广泛使用在生产者 - 消费者场景
 * 比如利用 BlockingQueue 来实现
 * 由于其提供的等待机制，我们可以少操心很多协调工作
 * @see cn.ebing.dog.api.thread.producerconsumer.Client
 * 用的是 BlockingDeque，也是类似的。
 */
public class ConsumerProducer {
	public static final String EXIT_MSG  = "Good bye!";
	public static void main(String[] args) {
// 使用较小的队列，以更好地在输出中展示其影响
		BlockingQueue<String> queue = new ArrayBlockingQueue<>(3);
		Producer producer = new Producer(queue);
		Consumer consumer = new Consumer(queue);
		new Thread(producer).start();
		new Thread(consumer).start();
	}


	static class Producer implements Runnable {
		private BlockingQueue<String> queue;
		public Producer(BlockingQueue<String> q) {
			this.queue = q;
		}

		@Override
		public void run() {
			for (int i = 0; i < 20; i++) {
				try{
					Thread.sleep(5L);
					String msg = "Message" + i;
					System.out.println("Produced new item: " + msg);
					queue.put(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			try {
				System.out.println("Time to say good bye!");
				queue.put(EXIT_MSG);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	static class Consumer implements Runnable{
		private BlockingQueue<String> queue;
		public Consumer(BlockingQueue<String> q){
			this.queue=q;
		}

		@Override
		public void run() {
			try{
				String msg;
				while(!EXIT_MSG.equalsIgnoreCase( (msg = queue.take()))){
					System.out.println("Consumed item: " + msg);
					Thread.sleep(10L);
				}
				System.out.println("Got exit message, bye!");
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}