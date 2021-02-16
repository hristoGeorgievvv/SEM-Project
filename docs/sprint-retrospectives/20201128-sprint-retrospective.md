#Sprint Retrospective, Iteration #1

Project: Student-house food management

Group: op27-sem54

| User story | Task | Assinged to | Estimated Effort  per Task | Actual Effortper Task | Done | Notes |
|-------------|------|-------------|----------------------------|-----------------------|------|-------|
|     #11     |  Database Communication    |  Robert & Luc & Maarten  |     30m   |   ~1h    |  ✔️    |       |
|     #11     |  Testing    |  Robert & Luc  |     3h   |   ~8h    |  ✔️    |       |
|     #11     |  Configurations and Controllers    |  Robert & Luc & Maarten  |     3h   |   ~18h    |  ✔️    |       |
|     #11     |  Entities    |  Luc  |     10m   |   ~10m    |  ✔️    |       |
|     #11     | JWT validation with key    |  Robert & Maarten  |     2.5h   |   ~4h    |  ✔️    |       |
|     #9     | Fridge Service Entities  |  Anton, Hristo, Lyuben  |     4h   |   ~6h    |  ✔️    |  |
|     #9     | DB Design & Business Logic (Draft)  |  Anton, Hristo, Lyuben  |     2h   |   ~3h    |  ✔️    |  |
|     #9     | DB Setup & Docker Registry setup  |  Lyuben  |     3h   |   ~3h    |  ✔️    |  |
|     #10     | Main service routing  |  Lyuben  |     3h   |   ~4h    |  ✔️    |  |
|     #9     | Setup Microservice & Pipeline  |  Hristo  |     2h   |   ~3h    |  ✔️    |  |
|     #9     | Transaction Architecture Research & Brainstorming  |  Anton  |     30m   |   ~2h    |  ✔️    |  |

## Main Problems Encountered
### Spring @Value annotation returning null values
- Description: We ran into an issue using Spring @Value annotations. These annotations were returning null values instead of the values defined in the actual application.properties file.
- Reaction: We temporarily decided not to use Spring @Value annotations.
### Microservice architecture routing
- Description: We ran into problems figuring out how to set up our microservice architecture. We had a basic idea of splitting up our system but the details turned out a bit more difficult than expected. It turned out that Netflix' Zuul wasn't supported by our current Spring version and we wouldn't be able to use it (and maybe we shouldn't, as it's too big for the scope of our project).
- Reaction: We switched to Spring Cloud and more precisely Spring Cloud Gateway. This turned our Main service in a very simple gateway that just routes user's requests to various other backend services. So far this seemse like a good decision but it could introduce limitations later.
### Failing Pipelines Due to Docker issues
- Description: Many of our pipelines were annoyingly failing due to an internal limit in Docker outside of our control. We couldn't rely on pipelines to automatically control/check our code, leading to a drop in efficiency.
- Reaction: We set up our own docker registry, as per @Lyuben Todorov's advice: `image: registry.glamav.systems/gradle:6.7-jdk11`
### Checkstyle woes
- Description: Checkstyle failed often due to wrong ordering of the imports. Time-consuming to fix, while contributing very little to overall code quality.
- Reaction: We removed checkstyle's rule enforcing alphabetical ordering of imports.
### How does JWT work
- Description: We at first had no idea how to properly do generate and give a JWT token.
- Reaction: We did a lot of research on how to it and what libraries to use and tried multiple different solutions before we ended on this one.

## Adjustments for the next Sprint Plan

<!---Motivate any adjustments that will be made for the next Sprint Plan.--->
#### Better architectural planning
We did well on organizing everything with user stories but we lacked some lower-level organization on the technical side. Some things weren't exactly clear from the start when they could've been. Not having in-person meetings definitely contributed to that, we definitely could've used a real white board or something to present our ideas better. Overall I think we should communicate more between tasks.