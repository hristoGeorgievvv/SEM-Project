# PMD Exclusions
We excluded 1 rule from the PMD configuration:
### DataflowAnomalyAnalysis
This rule, which has recently been deprecated, tracks local definitions, undefinitions and references to variables.

We decided to exclude this rule from the PMD checks as it resulted in `DU-Anomalies` that would require us to make our code less readable. The `DU-Anomalies` came up when creating variables to make code more readable.