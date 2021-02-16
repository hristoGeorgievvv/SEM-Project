We use Redis to send messages from the fridge to the main microservice. Therefore, we need to connect our microservice to Redis. Redis provides messages in JSON, so this needs to be processed by a method like `process(String json): void`. The main microservice can process a message with this method: `process(product: ProductEntity, userPortions: Map<UserEntity, Integer>): void`. In addition to that, the correct child of `ProcessCredits` has to be chosen as can be seen in the UML diagram.

To solve this problem, an adapter design pattern would be a good fit.

To implement this, we implemented an interface that has the `process(String json): void` method. This implementation is called by the `MessageSubscriber`, which receives the Redis message. The implementation uses Gson to de-serialize the JSON Based on the message, the `ReddisAdapter` then picks the right child of `ProcessCredits` to further process the message.

![If the image doesn't open, check out "docs/assignment_one/RedisAdapter.pdf"](docs/assignment_one/RedisAdapter.png)

The pattern is implemented here:
https://gitlab.ewi.tudelft.nl/cse2115/2020-2010/7-student-house-food-management/op27-sem54/-/tree/master/mainService/src/main/java/nl/tudelft/sem/sem54/mainservice/redis