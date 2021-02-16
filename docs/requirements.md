# Requirements
`Group OP27-SEM54`

Robert van Dijk - 5051177
Hristo Georgiev - 5002664
Luc Jonker - 4836111
Lyuben Todorov - 5093368
Anton Totomanov - 5002990
Maarten Weyns - 5076099
# Must Haves

### Functional Requirements

- Create and maintain user accounts
- Secure user authentication with (unique) id or username and password
- **A household has:**
    - A set of users
        - ... who can join and leave the household
    - A fridge with a collection of products
- **Every user has:**
    - An overview of their household's status
    - A credit balance
    - If user has < -50 credits, then they are flagged to do the shopping next time
- **Each product has:**
    - A number of portions (specified only by the product adder)
    - A total worth in credits (specified only by the product adder)
    - Expiration date
- **Authenticated Users** must be able to:
    - Add newly bought products
    - Take portions
    - 'Undo' taking portion
- Users with less than 50 credits should be 'red flagged' to go shopping next time
- Only product adder can specify number of portions and cost

### Non-Functional Requirements

- Built as a RESTful API
- Extendable with extra functionalities (modular)
- Microservice architecture for horizontal scalability
- Easy to integrate with other systems 
- Written in Java 11
- Written using Spring Boot and Gradle.

# Should Haves

### Functional Requirements

- Users cooking and eating together should have portions/credits distributed among the users who participated
- A reset functionality, which sets all credit balances to 0
- Products can be edited after adding (only by the product adder)
- Products have an expiration date
    - After a product expires the remaining credits are split equally between all household members
- When a product is removed, the remaining credits are split equally between the members who used the product.
- Users can see their current credit balance
- Strong authentication

### Non-Functional Requirements

- The application should use Spring Security

# Could Haves

- Support for multiple households.
- Users can reset password
- Users can change households

# Won't Have

- Graphical User Interface (Front-end)
- Take portions in increments different than 1
