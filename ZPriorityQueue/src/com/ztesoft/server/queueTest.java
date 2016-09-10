package com.ztesoft.server;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.ztesoft.queueManage.QueueManage;
import com.ztesoft.queueObject.QueueObject;

public class queueTest {


    
    static Random r=new Random(47);  
      
    public static void main2(String args[]){  
        final PriorityBlockingQueue q=new PriorityBlockingQueue();  
        ExecutorService se=Executors.newCachedThreadPool();  
        //execute producer  
        se.execute(new Runnable(){  
            public void run() {  
                int i=0;  
                while(true){  
                    q.put(new queueTest().new PriorityEntity(r.nextInt(10),i++));  
                    try {  
                        TimeUnit.MILLISECONDS.sleep(r.nextInt(1000));  
                    } catch (InterruptedException e) {  
                        // TODO Auto-generated catch block  
                        e.printStackTrace();  
                    }  
                }  
            }  
        });  
          
        //execute consumer  
        se.execute(new Runnable(){  
            public void run() {  
                while(true){  
                    try {  
                        System.out.println("take-- "+q.take().toString()+" left:-- ["+q.toString()+"]");  
                        try {  
                            TimeUnit.MILLISECONDS.sleep(r.nextInt(1000));  
                        } catch (InterruptedException e) {  
                            // TODO Auto-generated catch block  
                            e.printStackTrace();  
                        }  
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }  
        });  
        try {  
            TimeUnit.SECONDS.sleep(5);  
        } catch (InterruptedException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        System.out.println("shutdown");  
    }  
  
    
    class PriorityEntity implements Comparable<PriorityEntity> {  
  
        private int id=0;  
        private int priority;  
        private int index=0;  
      
        public PriorityEntity(int _priority,int _index) {  
            this.priority = _priority;  
            this.index=_index;  
        }  
          
        public String toString(){  
            return id+"# [index="+index+" priority="+priority+"]";  
        }  
      
        //数字小，优先级高  
        public int compareTo(PriorityEntity o) {  
            return this.priority > o.priority ? 1  
                    : this.priority < o.priority ? -1 : 0;  
        }  
      
        //数字大，优先级高  
    //  public int compareTo(PriorityTask o) {  
//          return this.priority < o.priority ? 1  
//                  : this.priority > o.priority ? -1 : 0;  
    //  }  
    }  
    
    static QueueManage qManage = new QueueManage();
    static Random random=new Random(417);
    static String[] qName=new String[]{"ll","aa","bb","cc"};
    public static void main(String[] args) throws InterruptedException {
    	
		Thread producerThreads[] = new Thread[10];
		for (int i = 0; i < producerThreads.length; i++) {
			producerThreads[i] = new Thread(new Runnable(){
				
				@Override
				public void run() {
					int se = random.nextInt(4);
					PriorityBlockingQueue<QueueObject> queue = qManage.getQueueByQName("queue"+qName[se]);
					if(queue==null){
						queue = qManage.create("queue"+qName[se]);
						System.out.println("================queue"+qName[se]);
					}
					
					for(int i = 0; i < 100; i++){
						QueueObject obj = new QueueObject();
						obj.setObj("我是队列quere"+qName[se]+"里的消息:"+i);
						obj.setQueueName("queue"+se);
						queue.put(obj);
					}

					
				}
				
			});
			producerThreads[i].start();
		}

		
		
		Thread.sleep(1000);
		
		PriorityBlockingQueue<QueueObject> queueComsumer = qManage.getQueueByQName("queueaa");
		System.out.println(">>>>>>"+queueComsumer.size());
		
		for(int i = 0; i < 100; i++){
			QueueObject obj = queueComsumer.poll();
			System.out.println(obj.getObj());
		}
		
		
    	
		
	}
    
    
}  
  
