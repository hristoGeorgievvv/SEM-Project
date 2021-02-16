# Usage of the Builder pattern
Surprisingly, the complexity of some of our classes grew beyond the ‘understand just by looking’ barrier even for such small requirements. This goes for their creation as well. We take advantage of the builder pattern in several classes we manually identified as having ‘fat constructors’. Here's a list of the refractored classes:
* [TakeoutRequestResult.java](fridge/src/main/java/nl/tudelft/sem/sem54/fridge/controller/pojo/TakeoutRequestResult.java)
* [Product.java](fridge/src/main/java/nl/tudelft/sem/sem54/fridge/domain/Product.java)
* [ProductTransaction.java](fridge/src/main/java/nl/tudelft/sem/sem54/fridge/domain/ProductTransaction.java)
* [ProductStatus.java](fridge/src/main/java/nl/tudelft/sem/sem54/fridge/redis/schema/ProductStatus.java)

Troughout our development of these classes we've tried to generally avoid inheritance wherever possible, and we've succeeded in doing that. Contrary to its representation in theory, we've actually found it quite avoidable in most of the cases for the better.

Here's a UML class diagram of one of the classes we've implemented the builder pattern on.
![If the image doesn't open, check out "docs/assignment_one/ProductStatus.png" ](docs/assignment_one/ProductStatus.png)

In the case of ProductStatus.java, we have a class which strictly depends on 2 others, the `MessageType` enum and the `Product` class. The builder allows us to create instances of our `ProductStatus` in a much cleaner and readable way.

