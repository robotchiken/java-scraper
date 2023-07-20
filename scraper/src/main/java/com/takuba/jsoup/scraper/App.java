package com.takuba.jsoup.scraper;

public class App
{
	public static final String urls[] = {
			//"ataque de los titanes",
			//"the boys",
			//"my hero academia",
			//"berserk",
			//"one punch man",
			//"x-men"
			"cuatro fantasticos"
			};
	
	
	public static void main( String[] args )
    {
		IndautorPageScrapper scrapper = new IndautorPageScrapper();
		for(int i = 0; i<urls.length;i++) {
			scrapper.findTitle(urls[i]);
		}
		scrapper.displayItems();
		/*
		System.out.println("I am a Kafka Consumer");

        String bootstrapServers = "127.0.0.1:9092";
        String groupId = "my-fifth-application";
        String topic = "demo_java";

        // create consumer configs
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // create consumer
        final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // get a reference to the current thread
        final Thread mainThread = Thread.currentThread();

        // adding the shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
            	System.out.println("Detected a shutdown, let's exit by calling consumer.wakeup()...");
                consumer.wakeup();

                // join the main thread to allow the execution of the code in the main thread
                try {
                    mainThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        try {

            // subscribe consumer to our topic(s)
            consumer.subscribe(Arrays.asList(topic));

            // poll for new data
            while (true) {
                ConsumerRecords<String, String> records =
                        consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, String> record : records) {
                	System.out.println("Key: " + record.key() + ", Value: " + record.value());
                	System.out.println("Partition: " + record.partition() + ", Offset:" + record.offset());
                	//scrapper.findTitle(record.value());
                }
            }

        } catch (WakeupException e) {
        	System.out.println("Wake up exception!");
            // we ignore this as this is an expected exception when closing a consumer
        } catch (Exception e) {
        	System.out.println("Unexpected exception");
        } finally {
            consumer.close(); // this will also commit the offsets if need be.
            System.out.println("The consumer is now gracefully closed.");
            scrapper.displayItems();
        }
		*/
    }
    
}
