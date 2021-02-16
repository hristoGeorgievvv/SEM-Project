# CSE2115 - Project


## Architecture 
For architecture description check [Architecture](./docs/assignment_one/Group-OP27-SEM54-Assignment-1.pdf).
(Rough image [here](./docs/high-level-microservice-idea.png))

### Docker
For info on running with Docker, see [Docker](./docs/docker.md).

## Documentation
For the documentation on the endpoints, see [endpoints](./docs/endpoints/).


## Testing
```
gradle test
```

To generate a coverage report:
```
gradle jacocoTestCoverageVerification
```


And
```
gradle jacocoTestReport
```
The coverage report is generated in: build/reports/jacoco/test/html, which does not get pushed to the repo. Open index.html in your browser to see the report. 

## Static analysis
```
gradle checkStyleMain
gradle checkStyleTest
gradle pmdMain
gradle pmdTest
```

## Notes
- You should have a local .gitignore file to make sure that any OS-specific and IDE-specific files do not get pushed to the repo (e.g. .idea). These files do not belong in the .gitignore on the repo.
- If you change the name of the repo to something other than template, you should also edit the build.gradle file.
- You can add issue and merge request templates in the .gitlab folder on your repo. 
