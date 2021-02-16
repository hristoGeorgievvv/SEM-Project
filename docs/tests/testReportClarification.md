# Test Coverage
### Coverage Overview per Microservice
|                             | Instruction Coverage | Branch Coverage|
|:---------------------------:|:--------------------:|:--------------:|
| Authentication Microservice | 95%                  | 95%            |
| Main Microservice           | 85%                  | 93%            |
| Fridge Microservice         | 80%                  | 100%            |

### Jacoco Test Reports
The JaCoCo test reports can be found in `docs/tests/jacocoReports`. This has seperate reports per microservice. The reports can be viewed by opening the `index.html` files.

### Exclusion of `config` and `configuration` packages
You may have noticed that we excluded the `config` and `configuration` packages in each microservice from the JaCoCo test coverage. This is done because these packages contain classes that are relevant to `SpringSecurity` elements, or these classes configure things like `Redis`.

In other words, these classes do not contain actual logic that can be independently tested. This does not mean that these classes go untested, as the correcness of these classes is asserted when testing endpoints etc, which we all tested.

##Integration tests
There is an integration test that tests a full user story including the redis connection between fridge and main, and the connection between main and auth.
For this test, the microservices have to run locally using docker-compose.