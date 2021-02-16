# Redis usage in this project
## What is redis?
> Redis is an open source (BSD licensed), in-memory data structure store, used as a database, cache and message broker. It supports data structures such as strings, hashes, lists, sets, sorted sets with range queries, bitmaps, hyperloglogs, geospatial indexes with radius queries and streams. 

It's like a really advanced java HashMap (KV-store) but it's a database that lives strictly in memory (so that it's very fast, relatively). KV stores are really useful for a couple of things but it really depends on the context.  For example, if we didn't use the 'highly advanced' JWT tokens with built-in expiration, an alternative could be storing the key-value pair with username:token in Redis with a certain expriation date.

## Pub-Sub
Another functionality of Redis is it's implementation of the [publish-subscribe](https://en.wikipedia.org/wiki/Publish%E2%80%93subscribe_pattern)  pattern. In a pub-sub network you have publishers, subscribers and channels. Logically, subscribers subscribe to channels (or topics), to which publishers publish messages to. A key thing to note is that both subscribers and publishers are unaware of eachother and this is not a request/reply model at all. Also, the broker (redis), simply broadcasts the publisher's messages in a fire and forget manner. Broadcasting is the case where the broadcaster **does not** care at all about the receivers of the message. It doesn't need an acknowledge, it doesn't keep track of listeners and it won't send you old messages if you missed any.
## In our project
We use redis pub-sub to communicate events from our fridge service to the main service. Those are events whose origin is on the fridge service and trigger **user balance** changes over on the main service. We treat the following cases:

* Product expiration
* Product depletion
* Product removal

Since all redis' pub-sub does is send strings (not some magical redis objects), we've decided to encode objects to JSON strings on the publisher side and similarly decode them back to POJOs on the subscriber side. Our format is the following: 
```java
/**
 * Object used for message-passing between main and fridge service.
 */
public class ProductStatus  {
    @SerializedName("message_type")
    private MessageType messageType;
    
    @SerializedName("owner_username")
    private String ownerUsername;
    
    @SerializedName("total_portions")
    private int totalPortions;
    
    @SerializedName("total_credit_value")
    private int totalCreditValue;
    
    @SerializedName("portions_per_user")
    private Map<String, Integer> portionsPerUser;
}

public enum MessageType {
    PRODUCT_FINISHED,
    PRODUCT_SPOILED,
    PRODUCT_REMOVED
}
```
This format covers all of our cases, as credit distribution requires the same amount of information each time.

On the publisher's side, events can simply sent like so:
```java
// Inject our publisher bean
@Autowired
private final RedisMessagePublisher productPublisher;

/**
.....
*/

// Library we use for serialization/deserialization
Gson gson = new Gson();

// Creating the POJO
ProductStatus productStatus =
        new ProductStatus(MessageType.PRODUCT_SPOILED, product);
// Converting it to a string   
String productStatusString = gson.toJson(productStatus);

// Sending it over.
productPublisher.publish(productStatusString);
```
On the subscriber's side (main service), we implement the following provided interface:
```java
public interface MessageListener {

	/**
	 * Callback for processing received objects through Redis.
	 *
	 * @param message message must not be {@literal null}.
	 * @param pattern pattern matching the channel (if specified) - can be {@literal null}.
	 */
	void onMessage(Message message, @Nullable byte[] pattern);
}

```
... and implement our desired functionality further
```java
public void onMessage(final Message message, final byte[] pattern) {

        String messageText = new String(message.getBody());

        redisAdapter.process(messageText);
}
```

Our `RedisAdapter` turns the string back into an object and decides on how to process the message, like so:
```java
public void process(String json) {
		// Deserialize
        ProductStatus productStatus = jsonToProductStatus(json);
        // Get the needed processor class depending on messageType
        ProcessCredits processor = getProcessor(productStatus);
        // Get the desired product entity from the main service's database
        ProductEntity productEntity = makeProduct(productStatus);
        
        Map<UserEntity, Integer> userPortions = makeUserPortionsMap(productStatus);

	// Do stuff
        try {
            processor.process(productEntity, userPortions);
        } catch (PortionsDoNotFitException e) {
            e.printStackTrace();
        }
}
```