# Sprint Retrospective, Iteration #3

Project: Student-house food management

Group: op27-sem54

| User story | Task                              | Assinged to  | Estimated Effort  per Task | Actual Effortper Task | Done | Notes |
| ---------- | ----------------------------      | ------------ | -------------------------- | --------------------- | ---- | ----- |
| #4         | User can see his own credits      | Robert       | 1h                         | ~30m                  | ✔️   |       |
| #3         | User can delete account           | Robert & Luc | 1h                         | ~2h                   | ✔️   |       |
| #12        | User can have meals together      | Maarten      | 4h                         | 6h                    | ✔️   |       |
| #31        | Docker config                     | Lyuben       | 3h                         | 5h                    | ✔️   |       |
| #28        | Fridge service user verification  | Hristo & Tony| 2h                         | 3h                    | ✔️   |      |
| #27        | (Fridge Endpoint) User can add product              | Hristo & Tony| 3h                         | 5h                    | ✔️   |      |
| #26, #17       | Fridge service refactor           | Hristo & Lyuben       | 2h                         | 5h                    | ✔️   |      |
| #15        | (Fridge Endpoint) User can edit existing products | Anton  | 1h                         | 1h                    | ✔️   |       |
| !28        | (Fridge) Massively expand test suite | Anton, Hristo  | 3h                         | 3h                    | ✔️   |       |
| !28 #17       | (Fridge Endpoint) User can remove product | Anton  | 30m  | 30m                    | ✔️   |       |
| #30        | Refactor Main / Auth endpoints    |     Luc | ~2h                         | ~1.5h                   |   |       |
## Main Problems Encountered
### Meals Endpoint
With the creation of the meals enpoint, we discovered that some fridge endpoints were not working how they were supposed to work yet. Luckily, our team communicated about that and we were able to fix the endpoints and get meals up and running!

### Merge request bloat
During the development of one of our features, #17, we found out a separate part of our code could be improved. This merge request for a simple feature turned into a much bigger refractor of the whole service. This led us to miss some pre-defined deadlines, however we did consider the extension of our deadline for project when making those decisions.

## Adjustments for the next Sprint Plan
Have a better separation of merge requests. Bloated merge requests are hard to review and merging changes to many separate components at once could lead to unexpecteded bugs later on. 